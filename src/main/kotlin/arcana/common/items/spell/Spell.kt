package arcana.common.items.spell

import arcana.common.capability.getMana
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

open class Spell {
    open val castSpeedMult = 1f

    open val pressCost: FloatArray = FloatArray(6)
    open val holdCost: FloatArray = FloatArray(6)

    open fun press(world: World, caster: LivingEntity): Boolean =
        (caster is PlayerEntity && caster.isCreative) || caster.getMana().tryConsume(pressCost)

    open fun hold(world: World, caster: LivingEntity, ticksPassed: Int): Boolean =
        (caster is PlayerEntity && caster.isCreative) || caster.getMana().tryConsume(holdCost)


    open fun release(world: World, caster: LivingEntity, ticksPassed: Int) {}
}