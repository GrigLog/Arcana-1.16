package arcana.common.events

import arcana.common.CommonData
import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import arcana.common.aspects.Aspects.EARTH
import arcana.common.aspects.Aspects.FIRE
import arcana.common.aspects.Aspects.WATER
import net.minecraft.util.RegistryKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome.Category
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type.COLD
import net.minecraftforge.common.BiomeDictionary.Type.HOT
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber
object WorldEvents {
    @SubscribeEvent
    fun loadBiome(event: BiomeLoadingEvent) {
        if (event.name == null) //it is possible, but when?
            return
        val biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, event.name!!)
        val types = BiomeDictionary.getTypes(biomeKey)
        val aspects = AspectList()
        for (type: BiomeDictionary.Type in types) {
            aspects.add(when(type) {
                HOT -> AspectList().add(FIRE, 1)
                COLD -> AspectList().add(WATER, 1)
                else -> AspectList()
            })
        }
        aspects.add(when(event.category) {
            Category.PLAINS -> AspectList().add(EARTH, 2)
            else -> AspectList()
        })
        val sum = aspects.sum().toFloat()
        if (sum == 0f)
            return
        val vis = FloatArray(6) {i -> aspects.getAmount(Aspects.PRIMAL[i]) * 1f/20f * 1f/sum * 6}
        CommonData.BIOME_VIS[biomeKey] = vis
    }
}