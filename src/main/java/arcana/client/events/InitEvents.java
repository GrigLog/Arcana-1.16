package arcana.client.events;

import arcana.client.gui.ResearchTableScreen;
import arcana.common.blocks.ModBlocks;
import arcana.common.containers.ModContainers;
import arcana.common.particles.MarkParticle;
import arcana.common.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitEvents {
    @SubscribeEvent
    static void regParticles(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ModParticles.markType, MarkParticle.Factory::new);
    }
    @SubscribeEvent
    static void setup(FMLClientSetupEvent event){
        RenderTypeLookup.setRenderLayer(ModBlocks.RESEARCH_TABLE_RIGHT, RenderType.cutout());

        ScreenManager.register(ModContainers.RESEARCH_TABLE, ResearchTableScreen::new);
    }
}
