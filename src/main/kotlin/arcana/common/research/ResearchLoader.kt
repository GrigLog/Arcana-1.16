package arcana.common.research

import arcana.Arcana
import arcana.utils.Util.withPath
import com.google.common.collect.ImmutableList
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.item.Item
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
class ResearchLoader : JsonReloadListener(GSON, "research") {
    override fun apply(files: Map<ResourceLocation, JsonElement>, resourceManager: IResourceManager, profiler: IProfiler) {
        ServerResearchManager.requirements.clear()
        ServerResearchManager.files = files
        files.forEach { (location, file) ->
            if (file.isJsonObject)
                parseServer(file.asJsonObject, location)
        }
        Arcana.logger.info("parsed researches on server")
    }

    companion object {
        val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        fun applyClient(files: Map<ResourceLocation, JsonElement>) {
            ClientResearchManager.books.clear()
            files.forEach { (location, file) ->
                if (file.isJsonObject)
                    parseClient(file.asJsonObject, location)
            }
            files.forEach { (location, file) ->
                if (file.isJsonObject)
                    parseParents(file.asJsonObject, location)
            }
            Arcana.logger.info("parsed researches on client")
        }

        fun parseServer(json: JsonObject, rl: ResourceLocation) {
            try {
                var ctr = 0
                for (el in json["sections"].asJsonArray) {
                    val section = el.asJsonObject
                    if (section["type"].asString == "requirements") {
                        val key = rl.withPath { "$it/" + ++ctr }
                        val reqs: List<Requirement> = Requirement.parse(section)
                        ServerResearchManager.requirements[key] = reqs
                    }
                }
            } catch (e: Exception) {
                Arcana.logger.error("Error parsing research file $rl on server.")
                e.printStackTrace()
            }
        }

        fun parseClient(json: JsonObject, rl: ResourceLocation) {
            try {
                val ctr = 0
                val entry = BookEntry()
                val sections: MutableList<BookSection> = ArrayList()
                for (el in json["sections"].asJsonArray) {
                    val section = el.asJsonObject
                    sections.add(BookSection.parse(section))
                }
                entry.sections = sections.toTypedArray()
                entry.x = json["x"].asByte
                entry.y = json["y"].asByte
                val icons: MutableList<Item> = ArrayList()
                for (el in json["icons"].asJsonArray)
                    icons.add(ForgeRegistries.ITEMS.getValue(ResourceLocation(el.asString))!!)
                entry.icons = icons.toTypedArray()
                ClientResearchManager.add(rl, entry)
            } catch (e: Exception) {
                Arcana.logger.error("Error parsing research file $rl on client.")
                e.printStackTrace()
            }
        }

        fun parseParents(json: JsonObject, rl: ResourceLocation) {
            try {
                val current = ClientResearchManager.get(rl)
                if (!json.has("parents")) {
                    current!!.parents = ImmutableList.of()
                    return
                }
                val parents: MutableList<BookEntry> = ArrayList()
                for (el in json["parents"].asJsonArray)
                    parents.add(ClientResearchManager.get(ResourceLocation(el.asString))!!)
                current!!.parents = ImmutableList.copyOf(parents)
            } catch (e: Exception) {
                Arcana.logger.error("Error parsing research file $rl on client.")
                e.printStackTrace()
            }
        }
    }
}