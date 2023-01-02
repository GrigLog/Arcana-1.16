package arcana.common

import net.minecraft.util.RegistryKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

object CommonData {
    val BIOME_VIS: MutableMap<RegistryKey<Biome>, FloatArray> = HashMap()
    private val DEFAULT_VIS = floatArrayOf(0.05f, 0.05f, 0.05f, 0.05f, 0.05f, 0.05f)
    fun getBiomeVis(biome: Biome) =
        BIOME_VIS.getOrDefault(RegistryKey.create(Registry.BIOME_REGISTRY, biome.registryName!!), DEFAULT_VIS)
}