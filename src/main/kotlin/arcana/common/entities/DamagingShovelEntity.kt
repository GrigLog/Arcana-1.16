package arcana.common.entities

import arcana.utils.Util.closestTo
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.abs

class DamagingShovelEntity(type: EntityType<out DamagingShovelEntity> = ModEntities.DAMAGING_SHOVEL, world: World) : ProjectileEntity(type, world) {
    companion object {
        const val LIFETIME = 20
    }
    constructor(caster: LivingEntity) : this(world=caster.level){
        moveTo(caster.x, caster.y + caster.eyeHeight, caster.z)
        shoot(caster.lookAngle.x, caster.lookAngle.y, caster.lookAngle.z, 0.15f, 0f)
        owner = caster
    }
    var age = 0

    override fun getAddEntityPacket() = NetworkHooks.getEntitySpawningPacket(this)

    override fun defineSynchedData() {}

    override fun onHitEntity(result: EntityRayTraceResult) {}

    override fun onHitBlock(result: BlockRayTraceResult) {
        super.onHitBlock(result)
        remove()
    }

    override fun tick() {
        if (age++ > LIFETIME) {
            remove()
        }
        val raytraceresult = ProjectileHelper.getHitResult(this) { target: Entity? -> canHitEntity(target) }
        if (raytraceresult.type != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            onHit(raytraceresult)
        }
        checkInsideBlocks()
        val newPos = position().add(deltaMovement)
        setPos(newPos.x, newPos.y, newPos.z)
        val normal = deltaMovement.cross(Vector3d(0.0, 1.0, 0.0)).normalize()
        val dirHor = Vector3d(deltaMovement.x, 0.0, deltaMovement.z).normalize()
        if (!level.isClientSide) {
            for (living in level.getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(2.0), { it != owner })) {
                val dist = living.boundingBox.closestTo(newPos).subtract(newPos)
                if (dist.lengthSqr() <= 4.0 && abs(normal.dot(dist)) < 0.3f) {
                    //living.hurt(DamageSource.thrown(owner, this), 1f)
                    living.hurt(DamageSource.GENERIC, 1f)
                    living.deltaMovement = deltaMovement.scale(0.5).add(0.0, 0.1, 0.0)
                    living.addEffect(EffectInstance(Effects.MOVEMENT_SLOWDOWN, 10, 6))
                }
            }
        }
        super.tick()
    }


}