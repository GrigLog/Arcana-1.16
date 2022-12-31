package arcana.common.research.requirements

import arcana.common.capability.getKnowledge
import arcana.common.research.Requirement
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

class SpecialRequirement : Requirement {
    var id: ResourceLocation? = null
    override fun satisfied(player: PlayerEntity): Boolean {
        return player.getKnowledge().specialRequirementsMet.contains(id)
    }

    override fun take(player: PlayerEntity) {}
    override fun deserialize(data: String) {
        id = ResourceLocation(data)
    }
}