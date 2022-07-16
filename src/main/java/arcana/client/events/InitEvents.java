package arcana.client.events;

import arcana.common.particles.MarkParticle;
import arcana.common.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitEvents {
    @SubscribeEvent
    static void regParticles(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ModParticles.markType, MarkParticle.Factory::new);
    }
}
