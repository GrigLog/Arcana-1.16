package arcana.common.research

import arcana.common.research.requirements.*
import com.google.gson.JsonObject
import net.minecraft.entity.player.PlayerEntity

interface Requirement {
    fun satisfied(player: PlayerEntity): Boolean
    fun take(player: PlayerEntity)
    fun deserialize(data: String)

    companion object {
        val types = mapOf(
            "item" to ::ItemRequirement,
            "item_tag" to ::TagRequirement,
            "special" to ::SpecialRequirement,
            "xp" to ::XpRequirement)

        fun parse(json: JsonObject): List<Requirement> {
            val reqs: MutableList<Requirement> = ArrayList()
            for (req in json["content"].asJsonArray) {
                reqs.add(parse(req.asString))
            }
            return reqs
        }

        fun parse(string: String): Requirement {
            val parts = string.split(" ")
            val r = types[parts[0]]!!.call()
            r.deserialize(parts[1])
            return r
        }
    }
}