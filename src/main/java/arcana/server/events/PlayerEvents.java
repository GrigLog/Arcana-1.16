package arcana.server.events;

import arcana.Arcana;
import arcana.common.capability.Knowledge;
import arcana.common.capability.Mana;
import arcana.common.capability.Marks;
import com.google.common.primitives.Floats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static arcana.common.capability.Marks.MARKS_RANGE;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    static void login(PlayerEvent.PlayerLoggedInEvent event){
        sendCaps(event.getPlayer());
    }
    @SubscribeEvent
    static void respawn(PlayerEvent.PlayerRespawnEvent event){
        sendCaps(event.getPlayer());
    }
    @SubscribeEvent
    static void changeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        sendCaps(event.getPlayer());
    }
    @SubscribeEvent
    static void copyCapsOnRespawn(PlayerEvent.Clone event) {
        PlayerEntity old = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        Mana.unchecked(old).copyTo(Mana.unchecked(player));
        Knowledge.unchecked(old).copyTo(Knowledge.unchecked(player));
    }

    static Map<UUID, ChunkPos> savedChunks = new HashMap<>();
    @SubscribeEvent
    static void enterChunk(EntityEvent.EnteringChunk event){
        if (!(event.getEntity() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        ChunkPos saved = savedChunks.getOrDefault(player.getUUID(), new ChunkPos(0, 0));
        if (Math.abs(saved.x - event.getNewChunkX()) >= MARKS_RANGE
            || Math.abs(saved.z - event.getNewChunkZ()) >= MARKS_RANGE){
            savedChunks.put(player.getUUID(), new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()));
            Marks.sendToClient(player);
        }
    }

    static void sendCaps(PlayerEntity player1) {
        ServerPlayerEntity player = (ServerPlayerEntity) player1;
        Marks.sendToClient(player);
        player.getCapability(Mana.CAPABILITY).resolve().ifPresent(mana -> mana.sendToClient(player));
        player.getCapability(Knowledge.CAPABILITY).resolve().ifPresent(knowledge -> knowledge.sendToClient(player));
    }

    static int syncTimer = 0;
    @SubscribeEvent
    static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            return;
        event.player.getCapability(Mana.CAPABILITY).resolve().ifPresent(mana -> {
            if (event.side == LogicalSide.SERVER && syncTimer++ > 60) {
                syncTimer = 0;
                mana.sendToClient((ServerPlayerEntity) event.player);
            }
            if (!event.player.isAlive())
                return;
            float[] biomeAura = new float[]{0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f};
            mana.add(biomeAura);
            Arcana.logger.info(Floats.asList(mana.values));
        });
    }
}
