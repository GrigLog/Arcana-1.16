package arcana.common.research.requirements

import arcana.common.research.Requirement
import net.minecraft.entity.player.PlayerEntity

class XpRequirement : Requirement {
    var minLevel = 0
    var takeLevel = 0
    override fun satisfied(player: PlayerEntity): Boolean {
        return player.experienceLevel >= minLevel
    }

    override fun take(player: PlayerEntity) {
        player.giveExperienceLevels(-takeLevel)
    }

    override fun deserialize(data: String) {
        val parts = data.split("-")
        minLevel = parts[0].toInt()
        takeLevel = parts[1].toInt()
    }
}