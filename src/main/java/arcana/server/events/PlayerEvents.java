package arcana.server.events;

import arcana.common.capability.Marks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static arcana.common.capability.Marks.MARKS_RANGE;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    static void login(PlayerEvent.PlayerLoggedInEvent event){
        Marks.sendToClient(event.getPlayer());
    }
    @SubscribeEvent
    static void respawn(PlayerEvent.PlayerRespawnEvent event){
        Marks.sendToClient(event.getPlayer());
    }
    @SubscribeEvent
    static void changeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        Marks.sendToClient(event.getPlayer());
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
}
