package arcana.common.items.spell

import arcana.common.entities.ChasingSkullEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

class ChasingSkull : Spell() {
    override fun press(world: World, caster: LivingEntity): Boolean {
        if (!super.press(world, caster))
            return false
        world.addFreshEntity(ChasingSkullEntity(world, caster))
        return true
    }
}