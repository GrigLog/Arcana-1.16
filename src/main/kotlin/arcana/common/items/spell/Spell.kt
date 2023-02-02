package arcana.common.items.spell

import arcana.common.capability.getMana
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemUseContext
import net.minecraft.world.World

open class Spell {
    open val castSpeedMult = 1f

    open val pressCost: FloatArray = FloatArray(6)
    open val holdCost: FloatArray = FloatArray(6)

    open fun press(world: World, caster: LivingEntity): Boolean =
        trySpendMana(caster, pressCost)

    open fun hold(world: World, caster: LivingEntity, ticksPassed: Int): Boolean =
        trySpendMana(caster, holdCost)

    open fun pressBlock(caster: LivingEntity, ctx: ItemUseContext): Boolean {
        return false
    }

    open fun release(world: World, caster: LivingEntity, ticksPassed: Int) {}

    companion object {
        fun trySpendMana(caster: LivingEntity, cost: FloatArray) =
            (caster is PlayerEntity && caster.isCreative) || caster.getMana().tryConsume(cost)
    }
}