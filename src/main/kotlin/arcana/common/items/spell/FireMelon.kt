package arcana.common.items.spell

import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects.FIRE
import net.minecraft.enchantment.ProtectionEnchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.Explosion
import net.minecraft.world.World

class FireMelon    //will be allowed to modify with a config
    (var damage: Float, var radius: Int, var burn_time: Int) : Spell() {
    override val castSpeedMult = 0.5f
    override val pressCost = AspectList().add(FIRE, 1).toArray()

    override fun press(world: World, caster: LivingEntity): Boolean {
        if (!super.press(world, caster))
            return false
        val x = caster.x
        val y = caster.y + 1
        val z = caster.z
        if (world.isClientSide) {
            world.playLocalSound(
                x,
                y,
                z,
                SoundEvents.GENERIC_EXPLODE,
                SoundCategory.BLOCKS,
                4.0f,
                (1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.2f) * 0.7f,
                false
            )
            var b = -Math.PI
            while (b < Math.PI) {
                val r = radius * Math.cos(b) / 8
                val ys = radius * Math.sin(b) / 8
                var a = 0.0
                while (a < Math.PI * 2) {
                    val xs = r * Math.sin(a)
                    val zs = r * Math.cos(a)
                    world.addParticle(ParticleTypes.FLAME, x, y, z, xs, ys, zs)
                    a += 0.314
                }
                b += 0.1
            }
        } else {
            val diam = (radius * 2.0f).toDouble()
            val x1 = MathHelper.floor(x - diam - 1.0)
            val x2 = MathHelper.floor(x + diam + 1.0)
            val y1 = MathHelper.floor(y - diam - 1.0)
            val y2 = MathHelper.floor(y + diam + 1.0)
            val z1 = MathHelper.floor(z - diam - 1.0)
            val z2 = MathHelper.floor(z + diam + 1.0)
            val list = world.getEntities(
                caster,
                AxisAlignedBB(x1.toDouble(), y1.toDouble(), z1.toDouble(), x2.toDouble(), y2.toDouble(), z2.toDouble())
            )
            //net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, this, list, f2);
            val pos = caster.position()
            caster.setSecondsOnFire(burn_time)
            for (entity in list) {
                if (!entity.ignoreExplosion()) {
                    val dist = MathHelper.sqrt(entity.distanceToSqr(pos)) / diam
                    if (dist <= 1.0) {
                        var to = Vector3d(entity.x, entity.eyeY, entity.z).subtract(pos)
                        val distTo = to.length()
                        if (distTo != 0.0) {
                            to = to.scale(1 / distTo)
                            val seenPercent = Explosion.getSeenPercent(pos, entity).toDouble()
                            val d10 = (1.0 - dist) * seenPercent
                            entity.hurt(DamageSource.IN_FIRE, damage)
                            entity.setSecondsOnFire(burn_time)
                            var knockback = d10 * 3
                            if (entity is LivingEntity) {
                                knockback = ProtectionEnchantment.getExplosionKnockbackAfterDampener(entity, d10)
                            }
                            entity.deltaMovement = entity.deltaMovement.add(to.scale(knockback))
                        }
                    }
                }
            }
        }
        return true
    }
}