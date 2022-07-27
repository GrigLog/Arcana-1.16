package arcana.client.events;

import arcana.client.gui.ResearchTableScreen;
import arcana.common.blocks.ModBlocks;
import arcana.common.containers.ModContainers;
import arcana.client.model.wand.WandModelLoader;
import arcana.common.items.spell.CapItem;
import arcana.common.items.spell.CoreItem;
import arcana.common.particles.MarkParticle;
import arcana.common.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static arcana.common.items.ModItems.FOCUS;
import static arcana.utils.Util.arcLoc;

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

    @SubscribeEvent
    static void regItemModelProperties(FMLClientSetupEvent event){
        ItemModelsProperties.register(FOCUS, arcLoc("style"),(stack, world, player)->stack.getTag().getInt("style"));
    }

    @SubscribeEvent
    static void regModels(ModelRegistryEvent event){
        ModelLoaderRegistry.registerLoader(arcLoc("wand_loader"), new WandModelLoader());
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event){
        for (CapItem cap : CapItem.CAPS.values())
            event.addSprite(arcLoc("models/wands/caps/"+cap.name));
        for(CoreItem core : CoreItem.CORES.values())
            event.addSprite(arcLoc("models/wands/cores/"+core.name));
        event.addSprite(arcLoc("models/wands/foci/wand_focus"));
        event.addSprite(arcLoc("models/wands/foci/wand_focus_overlay"));
        event.addSprite(arcLoc("models/wands/foci/wand_focus_t"));
    }
}
