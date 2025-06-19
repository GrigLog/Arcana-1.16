package arcana.common.entities

import net.minecraft.entity.CreatureEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.attributes.AttributeModifierManager
import net.minecraft.world.World

class CrimsonKnight(type: EntityType<out CrimsonKnight>, level: World) : CreatureEntity(type, level) {
    override fun getAttributes(): AttributeModifierManager {
        return super.getAttributes()
    }
}