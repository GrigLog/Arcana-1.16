package arcana.common.entities

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.network.NetworkHooks

class ArcanumEntity(type: EntityType<out ItemEntity> = ModEntities.ARCANUM, world: World)
    : ItemEntity(type, world) {
    constructor(original: ItemEntity) : this(world=original.level) {
        setPos(original.position().x, original.position().y, original.position().z)
        deltaMovement = original.deltaMovement
        setRot(original.rotationVector.x, original.rotationVector.y)
        setPickUpDelay(original.pickupDelay)
        item = original.item
    }

    override fun getAddEntityPacket() = NetworkHooks.getEntitySpawningPacket(this)

    override fun tick() {
        super.tick()
        if (!isAlive) return
        val res = ProjectileHelper.getHitResult(this) { e: Entity? -> true }
        if (res.type != RayTraceResult.Type.MISS
                && !ForgeEventFactory.onProjectileImpact(this, res)
                && res.type == RayTraceResult.Type.ENTITY) {
            val e = (res as EntityRayTraceResult).entity
            e.hurt(DamageSource.thrown(this, this), 10f)
        }
    }
}