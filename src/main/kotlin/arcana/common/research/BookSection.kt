package arcana.common.research

import arcana.common.research.sections.*
import com.google.gson.JsonObject

abstract class BookSection {
    abstract fun deserialize(json: JsonObject)

    companion object {
        val types = mapOf(
            "requirements" to ::RequirementSection,
            "string" to ::TextSection,
            "image" to ::ImageSection,
            "crafting" to ::CraftingTableSection,
            "arcane_crafting" to ::ArcaneCraftingTableSection)

        fun parse(json: JsonObject): BookSection {
            val s = types[json["type"].asString]!!.call()
            s.deserialize(json)
            return s
        }
    }
}