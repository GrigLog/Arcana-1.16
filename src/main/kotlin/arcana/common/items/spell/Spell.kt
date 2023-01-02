package arcana.common.items.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

open class Spell {
    open val castSpeedMult = 1f

    open fun press(world: World, caster: LivingEntity) {}
    open fun release(world: World, caster: LivingEntity, ticksPassed: Int) {}
}