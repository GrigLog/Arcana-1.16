package arcana.common.entities

import arcana.utils.Util.closestTo
import arcana.utils.Util.eyePosition
import net.minecraft.enchantment.ProtectionEnchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.DamageSource
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

class FireMelonEntity: Entity {
    val damage: Float
    val radius: Float
    val hitEntities = mutableListOf<Entity>()

    constructor(type: EntityType<out FireMelonEntity> = ModEntities.FIRE_MELON, world: World) : super(type, world) {
        damage = 3f
        radius = 3f
    }

    constructor(caster: LivingEntity, damage: Float, radius: Float) : super(ModEntities.FIRE_MELON, caster.level){
        moveTo(caster.eyePosition())
        this.damage = damage
        this.radius = radius
    }

    companion object {
        const val LIFETIME = 8
    }
    override fun defineSynchedData() {}
    override fun readAdditionalSaveData(pCompound: CompoundNBT) {}
    override fun addAdditionalSaveData(pCompound: CompoundNBT) {}
    override fun getAddEntityPacket() = NetworkHooks.getEntitySpawningPacket(this)

    var age = 0

    override fun tick() {
        if (++age > LIFETIME) {
            remove()
            return
        }
        if (level.isClientSide)
            return
        val pos = position()
        val list = level.getEntities(this, AxisAlignedBB.unitCubeFromLowerCorner(pos).inflate(radius.toDouble() + 1.0))
        for (entity in list) {
            if (!entity.ignoreExplosion() && entity !in hitEntities) {
                val dist = entity.boundingBox.closestTo(pos).distanceTo(pos) / radius
                if (dist <= 1.0 / LIFETIME * age) {
                    var to = Vector3d(entity.x, entity.eyeY, entity.z).subtract(pos)
                    val distTo = to.length()
                    if (distTo != 0.0) {
                        to = to.scale(1 / distTo)
                        val seenPercent = Explosion.getSeenPercent(pos, entity).toDouble()
                        val fakeDamage = (1.0 - dist) * seenPercent
                        entity.hurt(DamageSource.IN_FIRE, damage)
                        entity.setSecondsOnFire(4)
                        var knockback = fakeDamage * 3
                        if (entity is LivingEntity) {
                            knockback = ProtectionEnchantment.getExplosionKnockbackAfterDampener(entity, fakeDamage)
                        }
                        entity.deltaMovement = entity.deltaMovement.add(to.scale(knockback))
                    }
                    hitEntities.add(entity)
                }
            }
        }
    }

}