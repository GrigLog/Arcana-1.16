package arcana.client.events;

import arcana.common.capability.Marks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

@Mod.EventBusSubscriber
public class RenderEvents {
    @SubscribeEvent
    static void marks(RenderWorldLastEvent event){
        ClientWorld world = Minecraft.getInstance().level;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (world == null || player == null)
            return;
        if (player.tickCount % 20 != 0)
            return;
        Marks cap = world.getCapability(Marks.CAPABILITY).resolve().orElse(null);
        List<BlockPos> positions = cap.positions;
        positions.forEach(pos ->
            event.getContext().addParticle(ParticleTypes.FLAME, true, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 0, 0, 0));
    }
}
