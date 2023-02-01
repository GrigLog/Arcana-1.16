package arcana.common.items.spell

import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PotionEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.Effect
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects.*
import net.minecraft.potion.PotionUtils
import net.minecraft.world.World

class RandomPotion : Spell() {
    companion object {
        val GOOD_EFFECTS = arrayOf(MOVEMENT_SPEED, DAMAGE_BOOST, HEAL, JUMP,
            REGENERATION, DAMAGE_RESISTANCE, FIRE_RESISTANCE, WATER_BREATHING, INVISIBILITY, ABSORPTION, SLOW_FALLING)
        val BAD_EFFECTS = arrayOf(HARM, CONFUSION, BLINDNESS, HUNGER, WEAKNESS, POISON, WITHER)
    }
    override val pressCost = AspectList().add(Aspects.WATER, 1).toArray()

    override fun press(world: World, caster: LivingEntity): Boolean {
        if (!super.press(world, caster))
            return false
        if (!world.isClientSide) {
            val effect: Effect = when(world.random.nextFloat()) {
                in 0.5..1.0 -> GOOD_EFFECTS.random()
                else -> BAD_EFFECTS.random()
            }
            val effectInstence = EffectInstance(effect,300, 1)
            val stack = ItemStack(Items.POTION).let{PotionUtils.setCustomEffects(it, listOf(effectInstence))}
            val entity = PotionEntity(world, caster)
            entity.item = stack
            entity.shootFromRotation(caster, caster.xRot, caster.yRot, -20.0f, 0.5f, 1.0f)
            world.addFreshEntity(entity)
        }
        return true
    }
}