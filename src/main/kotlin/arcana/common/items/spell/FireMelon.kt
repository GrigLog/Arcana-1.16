package arcana.common.items.spell

import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects.FIRE
import arcana.common.entities.FireMelonEntity
import arcana.common.particles.LinearFireParticle
import arcana.utils.Util.eyePosition
import net.minecraft.entity.LivingEntity
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World
import java.lang.Math.PI

class FireMelon    //will be allowed to modify with a config
    (val damage: Float, val radius: Float) : Spell() {
    override val castSpeedMult = 0.5f
    override val pressCost = AspectList().add(FIRE, 1).toArray()

    override fun press(world: World, caster: LivingEntity): Boolean {
        if (!super.press(world, caster))
            return false
        val pos = caster.eyePosition()
        if (world.isClientSide) {
            world.playLocalSound(pos.x, pos.y, pos.z,
                SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS,
                4.0f, (1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.2f) * 0.7f,
                false)
            val speed = radius / 8f
            for (i in 0 until 60) {
                val theta = PI * (-0.5f + i / 60f)
                val speedCos = speed * Math.cos(theta)
                val ys = speed * Math.sin(theta)
                for (j in 0 until 20) {
                    val phi = 2 * PI * j / 20f
                    val xs = speedCos * Math.sin(phi)
                    val zs = speedCos * Math.cos(phi)
                    world.addParticle(LinearFireParticle.Data(), pos.x, pos.y, pos.z, xs, ys, zs)
                }
            }
        } else {
            world.addFreshEntity(FireMelonEntity(caster, damage, radius))
        }
        return true
    }
}