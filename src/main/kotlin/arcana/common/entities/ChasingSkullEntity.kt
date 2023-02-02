package arcana.common.entities

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.network.NetworkHooks
import java.lang.Math.PI
import kotlin.math.pow

class ChasingSkullEntity(type: EntityType<out ChasingSkullEntity> = ModEntities.CHASING_SKULL, world: World)
    : ProjectileEntity(type, world) {
    companion object {
        const val SPEED_MIN: Double = 0.15
        const val SPEED_MAX: Double = 0.3
        const val ROTATION: Double = 0.1
        const val TICKS_INITIAL = 20
        const val TICKS_LIFETIME = 200
    }
    var ticksLived = 0
    constructor(world: World, caster: LivingEntity) : this(ModEntities.CHASING_SKULL, world) {
        owner = caster
        moveTo(caster.x, caster.y + caster.eyeHeight, caster.z, 180 + caster.yRot, caster.xRot)
        deltaMovement = caster.lookAngle.scale(SPEED_MIN)
    }


    override fun getAddEntityPacket() = NetworkHooks.getEntitySpawningPacket(this)

    override fun defineSynchedData() {}

    override fun tick() {
        if (ticksLived++ > TICKS_LIFETIME) {
            remove()
            return
        }
        //Arcana.logger.info("skull speed: " + deltaMovement.x + " " + deltaMovement.y + " " + deltaMovement.z)
        val owner = owner
        if (level.isClientSide || (owner == null || !owner.removed) && level.hasChunkAt(blockPosition())) {
            super.tick()

            val raytraceresult = ProjectileHelper.getHitResult(this) { pTarget: Entity? -> canHitEntity(pTarget) }
            if (raytraceresult.type != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                onHit(raytraceresult)
            }
            checkInsideBlocks()
            val speed = deltaMovement
            val x2 = x + speed.x
            val y2 = y + speed.y
            val z2 = z + speed.z
            //Arcana.logger.info("pitch:$xRot yaw:$yRot")
            //"xRot" is actually pitch!!!
            //"yRot" is actually yaw!!!
            xRot = lerpRotation(xRotO, -(MathHelper.atan2(speed.y, Math.sqrt(speed.x * speed.x + speed.z * speed.z)) * (180f / Math.PI.toFloat()).toDouble()).toFloat())
            yRot = lerpRotation(yRotO, 180 -(MathHelper.atan2(speed.x, speed.z) * (180f / Math.PI.toFloat()).toDouble()).toFloat())


            if (this.isInWater) {
                for (i in 0..3) {
                    val f1 = 0.25f
                    level.addParticle(ParticleTypes.BUBBLE, x2 - speed.x * 0.25, y2 - speed.y * 0.25, z2 - speed.z * 0.25, speed.x, speed.y, speed.z)
                }
                //f = 0.8f
            }

            if (ticksLived > TICKS_INITIAL) {
                var newSpeed = SPEED_MIN
                var dir = speed.normalize()
                val lookPoint = position().add(dir.scale(1.0))
                val closestLiving = level.getEntities(this, boundingBox.inflate(8.0), { it is LivingEntity })
                    .minByOrNull { it.distanceToSqr(lookPoint) }
                if (closestLiving != null) {
                    val d =
                        closestLiving.position().add(0.0, closestLiving.eyeHeight.toDouble(), 0.0).subtract(position())
                    if (d != Vector3d.ZERO) {
                        val cos = MathHelper.clamp(dir.dot(d) / d.length(), -1.0, 1.0)
                        val x = 1.0 - MathHelper.clamp(Math.acos(cos) / (PI / 2), 0.0, 1.0)
                        //println("cos: $cos, acos: ${Math.acos(cos)}, x: $x")
                        newSpeed = MathHelper.lerp(x.pow(1.0), SPEED_MIN, SPEED_MAX)
                        var proj = d.subtract(dir.scale(dir.dot(d)))
                        if (proj.length() > 0.001) {
                            proj = proj.normalize().scale(Math.min(proj.length(), ROTATION))
                            dir = dir.add(proj).normalize()
                        }
                    }
                }
                deltaMovement = dir.scale(newSpeed)
                for (skull in level.getEntities(this, boundingBox.inflate(3.0), {it is ChasingSkullEntity})) {
                    val d = skull.position().subtract(position())
                    if (d != Vector3d.ZERO)
                        deltaMovement = deltaMovement.subtract(d.scale(Math.min(0.01, 0.01 / d.length() / d.length())))
                }
            }
            //level.addParticle(this.trailParticle, x2, y2 + 0.5, z2, 0.0, 0.0, 0.0)
            setPos(x2, y2, z2)
        } else {
            this.remove()
        }
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return if (ticksLived > TICKS_INITIAL) super.canCollideWith(entity) else false
    }

    override fun onHitEntity(result: EntityRayTraceResult) {
        if (result.entity !is LivingEntity)
            return
        val living = result.entity as LivingEntity
        if (living.isInvertedHealAndHarm) {
            living.heal(1f)
        } else {
            living.hurt(DamageSource.MAGIC, 10f)
        }
        remove()
    }

    override fun onHitBlock(result: BlockRayTraceResult) {
        val blockstate = level.getBlockState(result.blockPos)
        blockstate.onProjectileHit(level, blockstate, result, this)
        val dir = result.direction.normal
        deltaMovement = deltaMovement.multiply(1.0 - dir.x * 2.0, 1.0 - dir.y * 2.0, 1.0 - dir.z * 2.0)
        //remove()
    }
}