package arcana.server.events;

import arcana.common.capability.Marks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    static void login(PlayerEvent.PlayerLoggedInEvent event){
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        player.level.getCapability(Marks.CAPABILITY).resolve().orElse(null).sendToClient(player);
    }
    @SubscribeEvent
    static void respawn(PlayerEvent.PlayerRespawnEvent event){
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        player.level.getCapability(Marks.CAPABILITY).resolve().orElse(null).sendToClient(player);
    }
    @SubscribeEvent
    static void changeDim(PlayerEvent.PlayerChangedDimensionEvent event){
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        player.level.getCapability(Marks.CAPABILITY).resolve().orElse(null).sendToClient(player);
    }
}
