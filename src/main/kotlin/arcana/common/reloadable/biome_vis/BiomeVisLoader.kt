package arcana.common.reloadable.biome_vis

import arcana.Arcana
import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import arcana.common.reloadable.biome_vis.BiomeVis.BIOME_VIS
import arcana.utils.Util.arcLoc
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.registries.ForgeRegistries

class BiomeVisLoader : JsonReloadListener(GSON, "biomes_vis") {
    override fun apply(files: Map<ResourceLocation, JsonElement>, resourceManager: IResourceManager, profiler: IProfiler) {
        Companion.files = files
        applyCommon(files)
    }

    companion object {
        var files: Map<ResourceLocation, JsonElement> = HashMap()
        val TYPES_VIS: MutableMap<BiomeDictionary.Type, AspectList> = HashMap()
        val CATEGORIES_VIS: MutableMap<Biome.Category, AspectList> = HashMap()
        val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

        fun applyCommon(files: Map<ResourceLocation, JsonElement>) {
            BIOME_VIS.clear()
            TYPES_VIS.clear()
            CATEGORIES_VIS.clear()
            files.forEach { (location, file) ->
                if (file.isJsonObject)
                    parseCommon(file.asJsonObject, location)
            }
            for ((biomeKey, biome) in ForgeRegistries.BIOMES.entries) {
                val types = BiomeDictionary.getTypes(biomeKey)
                val aspects = AspectList()
                for (type: BiomeDictionary.Type in types)
                    aspects.add(TYPES_VIS.getOrDefault(type, listOf()))
                aspects.add(CATEGORIES_VIS.getOrDefault(biome.biomeCategory, listOf()))
                val sum = aspects.sum().toFloat()
                if (sum == 0f)
                    continue
                val vis = FloatArray(6) {i -> aspects.getAmount(Aspects.PRIMAL[i]) * 1f/20f * 1f/sum * 6}
                BIOME_VIS[biomeKey] = vis
            }
            Arcana.logger.info("Parsed biomes_vis.")
        }

        fun parseCommon(json: JsonObject, rl: ResourceLocation) {
            try {
                if (rl.path.endsWith("types")) {
                    for ((typeName, aspectsJson) in json.entrySet()) {
                        val type = BiomeDictionary.Type.getType(typeName)
                        val aspects = AspectList()
                        for ((aspectName, amount) in aspectsJson.asJsonObject.entrySet()) {
                            val aspect = Aspects.get(arcLoc(aspectName))
                            if (!(Aspects.PRIMAL.contains(aspect)))
                                throw Exception("Only primal aspects can be regenerated!")
                            aspects.add(aspect, amount.asInt)
                        }
                        TYPES_VIS[type] = aspects
                    }
                }
            } catch (e: Exception) {
                Arcana.logger.error("Error parsing biome vis file $rl.")
                e.printStackTrace()
            }
        }
    }
}