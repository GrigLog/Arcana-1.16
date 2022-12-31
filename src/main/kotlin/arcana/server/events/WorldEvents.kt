package arcana.server.events

import arcana.server.worldgen.ModFeatures
import net.minecraft.util.RegistryKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.gen.FlatChunkGenerator
import net.minecraft.world.gen.settings.StructureSeparationSettings
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE)
object WorldEvents {
    @SubscribeEvent
    fun loadBiome(event: BiomeLoadingEvent) {
        if (event.name == null) //it is possible, but when?
            return
        val biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, event.name!!)
        if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.OVERWORLD)) {
            event.generation
                .addStructureStart(ModFeatures.towerWater.configured)
                .addStructureStart(ModFeatures.towerEarth.configured)
                .addStructureStart(ModFeatures.towerAir.configured)
        } else if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.NETHER)) {
            event.generation.addStructureStart(ModFeatures.towerFire.configured)
        } else if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.END)) {
            event.generation
                .addStructureStart(ModFeatures.towerOrder.configured)
                .addStructureStart(ModFeatures.towerChaos.configured)
        }
    }

    @SubscribeEvent
    fun loadWorld(event: WorldEvent.Load) {
        if (event.world.isClientSide) return
        val world = event.world as ServerWorld
        val dim = world.dimension()
        if (world.chunkSource.getGenerator() is FlatChunkGenerator && dim == World.OVERWORLD) return
        val config = world.chunkSource.generator.settings.structureConfig()
        config[ModFeatures.towerAir] = StructureSeparationSettings(50, 35, -485136232)
        config[ModFeatures.towerWater] = StructureSeparationSettings(50, 35, -851473679)
        config[ModFeatures.towerEarth] = StructureSeparationSettings(50, 35, 992046760)
        config[ModFeatures.towerFire] = StructureSeparationSettings(40, 30, 602813591)
        config[ModFeatures.towerOrder] = StructureSeparationSettings(40, 30, 808644543)
        config[ModFeatures.towerChaos] = StructureSeparationSettings(40, 30, -148534866)
    }
}