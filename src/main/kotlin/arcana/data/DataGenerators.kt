package arcana.data

import arcana.Arcana
import net.minecraftforge.common.data.ForgeBlockTagsProvider
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object DataGenerators {
    @SubscribeEvent
    fun gatherData(evt: GatherDataEvent) {
        Arcana.logger.info("gatherData")
        val helper = evt.existingFileHelper
        val gen = evt.generator
        gen.addProvider(ItemModelProvider(gen, helper))
        gen.addProvider(BlockstateProvider(gen, helper))
        gen.addProvider(ItemTagsProvider(gen, ForgeBlockTagsProvider(gen, helper), helper))
        gen.addProvider(ItemAspectsProvider(gen))
    }
}