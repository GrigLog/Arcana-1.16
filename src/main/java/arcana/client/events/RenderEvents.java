package arcana.client.events;

import arcana.common.aspects.AspectUtils;
import arcana.common.capability.Marks;
import arcana.common.particles.MarkParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RenderEvents {
    @SubscribeEvent
    static void marks(RenderWorldLastEvent event){
        ClientWorld world = Minecraft.getInstance().level;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (world == null || player == null)
            return;
        if (player.tickCount % 15 != 0)
            return;
        Marks cap = world.getCapability(Marks.CAPABILITY).resolve().orElse(null);
        for (int i = 0; i < AspectUtils.primalAspects.length; i++) {
            for (BlockPos pos : cap.positions[i]){
                for (int j = 0; j < 3; j++)
                    world.addParticle(new MarkParticle.Data(AspectUtils.primalAspects[i]),
                        pos.getX() + world.random.nextFloat(), pos.getY() + 0.3, pos.getZ() + world.random.nextFloat(),
                        0, 0.1, 0);
            }
        }
    }
}