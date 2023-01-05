package arcana.common.items.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World

class WaterSpray : Spell() {
    override val castSpeedMult = 0.6f
    override val holdCost = floatArrayOf(0f, 0.05f, 0f, 0f, 0f, 0f)

    override fun hold(world: World, caster: LivingEntity, ticksPassed: Int): Boolean {
        if (!super.hold(world, caster, ticksPassed))
            return false
        val castFrom = Vector3d(caster.x, caster.eyeY - 0.3, caster.z)
        val box = AxisAlignedBB.ofSize(RANGE * 2.5, RANGE * 2.5, RANGE * 2.5).move(castFrom)
        val res = ProjectileHelper.getEntityHitResult(caster, castFrom, castFrom.add(caster.lookAngle.scale(RANGE * 2.5)), box, { e->true}, RANGE * RANGE)
        val speed = caster.lookAngle.scale(SPEED)
        for (i in 0..5) {
            val rand = Vector3d(world.random.nextDouble() - 0.5, world.random.nextDouble() - 0.5, world.random.nextDouble() - 0.5)
            val pos = castFrom.add(rand.scale(0.2))
            world.addParticle(ParticleTypes.BUBBLE_POP, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
        }
        val playerPushSpeed = caster.lookAngle.scale(-PLAYER_PUSH_SPEED)
        caster.deltaMovement = caster.deltaMovement.add(playerPushSpeed)

        if (res != null) {
            val enemyPushSpeed = caster.lookAngle.scale(PLAYER_PUSH_SPEED * 4)
            res.entity.deltaMovement = res.entity.deltaMovement.add(enemyPushSpeed)
        }
        return true
    }

    companion object {
        const val SPEED = 0.5
        const val PLAYER_PUSH_SPEED = 0.04
        const val RANGE = 5.0
    }
}