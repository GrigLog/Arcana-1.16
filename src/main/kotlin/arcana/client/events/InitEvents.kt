package arcana.client.events

import arcana.client.ClientPaths.CAPS_3D
import arcana.client.ClientPaths.CORES_3D
import arcana.client.gui.ResearchTableScreen
import arcana.client.model.wand.WandModelLoader
import arcana.client.render.ChasingSkullRenderer
import arcana.client.render.DamagingShovelRenderer
import arcana.common.blocks.ModBlocks
import arcana.common.containers.ModContainers
import arcana.common.entities.ModEntities
import arcana.common.items.ModItems
import arcana.common.items.wand.CapItem
import arcana.common.items.wand.CoreItem
import arcana.common.particles.MarkParticle
import arcana.common.particles.ModParticles
import arcana.utils.Util
import arcana.utils.Util.toInt
import arcana.utils.Util.withPath
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.item.ItemModelsProperties
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object InitEvents {
    @SubscribeEvent
    fun regParticles(event: ParticleFactoryRegisterEvent) {
        Minecraft.getInstance().particleEngine.register(ModParticles.MARK_TYPE, MarkParticle::Factory)
        //Minecraft.getInstance().particleEngine.register(ModParticles.WATER_TYPE, MarkParticle)
    }

    @SubscribeEvent
    fun setup(event: FMLClientSetupEvent) {
        RenderTypeLookup.setRenderLayer(ModBlocks.RESEARCH_TABLE_RIGHT, RenderType.cutout())

        ScreenManager.register(ModContainers.RESEARCH_TABLE, ::ResearchTableScreen)

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ARCANUM)
            { manager -> ItemRenderer(manager, Minecraft.getInstance().itemRenderer) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CHASING_SKULL, ::ChasingSkullRenderer)
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DAMAGING_SHOVEL, ::DamagingShovelRenderer)
    }

    @SubscribeEvent
    fun regItemModelProperties(event: FMLClientSetupEvent) {
        ItemModelsProperties.register(ModItems.FOCUS, Util.arcLoc("style"))
            { stack, world, player -> stack.tag!!.getInt("style").toFloat() }
        ItemModelsProperties.register(ModItems.ARCANUM, Util.arcLoc("open"))
            { stack, world, player -> stack.orCreateTag.getBoolean("open").toInt().toFloat() }
    }

    @SubscribeEvent
    fun regModels(event: ModelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(Util.arcLoc("wand_loader"), WandModelLoader())
    }

    @SubscribeEvent
    fun onTextureStitch(event: TextureStitchEvent.Pre) {
        for ((rl, cap) in CapItem.CAPS)
            event.addSprite(rl.withPath { CAPS_3D + it })
        for ((rl, core) in CoreItem.CORES) {
            event.addSprite(rl.withPath { CORES_3D + it })
            //event.addSprite(rl.withPath { HUD_CORES + it })
        }
        //for (a in Aspects.primal)
            //event.addSprite(a.id.withPath { HUD_VIS + it })
        event.addSprite(Util.arcLoc("models/wands/foci/wand_focus"))
        event.addSprite(Util.arcLoc("models/wands/foci/wand_focus_overlay"))
        event.addSprite(Util.arcLoc("models/wands/foci/wand_focus_t"))
    }
}