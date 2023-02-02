package arcana.common.items.spell

import arcana.common.entities.DamagingShovelEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

class DamagingShovel : Spell() {
    override fun press(world: World, caster: LivingEntity): Boolean {
        if (!super.press(world, caster)) return false
        if (!world.isClientSide) {
            world.addFreshEntity(DamagingShovelEntity(caster))
        }
        return true
    }
}