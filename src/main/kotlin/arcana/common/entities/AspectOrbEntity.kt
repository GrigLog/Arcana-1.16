package arcana.common.entities

import arcana.Arcana
import arcana.common.aspects.AspectStack
import arcana.common.aspects.Aspects
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.MoverType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.network.PacketBuffer
import net.minecraft.tags.FluidTags
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.math.max

/** [net.minecraft.entity.item.ExperienceOrbEntity] for reference. Also [net.minecraft.entity.projectile.ArrowEntity] */
class AspectOrbEntity(type: EntityType<out AspectOrbEntity>, level: World) : Entity(type, level), IEntityAdditionalSpawnData {
    var aspectStack: AspectStack = AspectStack(Aspects.FIRE, 1)
    var age = 0

    constructor(level: World, pos: Vector3d, value: AspectStack) : this(ModEntities.ASPECT_ORB, level) {
        setPos(pos.x, pos.y, pos.z)
        aspectStack = value
        deltaMovement = Vector3d(0.0, 1.0 / value.amount, 0.0)
        isNoGravity = true  // does not actually do anything since tick() is overriden but whatever
    }

    override fun tick() {
        super.tick()

        xo = x
        yo = y
        zo = z
        if (isEyeInFluid(FluidTags.WATER)) {
            deltaMovement = deltaMovement.scale(0.99)
        }

        if (level.getFluidState(blockPosition()).`is`(FluidTags.LAVA)) {
            setDeltaMovement(((random.nextFloat() - random.nextFloat()) * 0.2f).toDouble(), 0.2, ((random.nextFloat() - random.nextFloat()) * 0.2f).toDouble())
            playSound(SoundEvents.GENERIC_BURN, 0.4f, 2.0f + random.nextFloat() * 0.4f)
        }

        if (!level.noCollision(boundingBox)) {
            moveTowardsClosestSpace(x, (boundingBox.minY + boundingBox.maxY) / 2.0, z)
        }
        // XP orb code
        /*
        val d0 = 8.0
        if (followingTime < tickCount - 20 + id % 100) {
            if (followingPlayer == null || followingPlayer.distanceToSqr(this) > 64.0) {
                followingPlayer = level.getNearestPlayer(this, 8.0)
            }

            followingTime = tickCount
        }

        if (followingPlayer != null && followingPlayer.isSpectator()) {
            followingPlayer = null
        }

        if (followingPlayer != null) {
            val vector3d =
                Vector3d(followingPlayer.getX() - x, followingPlayer.getY() + followingPlayer.getEyeHeight()
                    .toDouble() / 2.0 - y, followingPlayer.getZ() - z)
            val d1 = vector3d.lengthSqr()
            if (d1 < 64.0) {
                val d2 = 1.0 - sqrt(d1) / 8.0
                deltaMovement = deltaMovement.add(vector3d.normalize().scale(d2 * d2 * 0.1))
            }
        }*/

        /*
        val SLOW_COEFF = 0.9
        move(MoverType.SELF, deltaMovement)
        var slipperiness = 1f
        if (onGround) {
            val pos = BlockPos(x, y - 1.0, z)
            slipperiness = level.getBlockState(pos).getSlipperiness(level, pos, this)
        }

        deltaMovement = deltaMovement.multiply(slipperiness.toDouble(), 1.0, slipperiness.toDouble()).scale(SLOW_COEFF)
        if (onGround) {
            deltaMovement = deltaMovement.multiply(1.0, -0.9, 1.0)
        }*/

        // dragAcceleration = c * v^2 / aspectStack.amount
        // dragAcceleration/v = c * v / aspectStack.amount
        // v - dragAcceleration = v(1 - dragAcceleration/v)
        move(MoverType.SELF, deltaMovement)
        if (level.isClientSide)
            Arcana.logger.info(deltaMovement.length())
        val DRAG_COEFF = 0.5
        deltaMovement = deltaMovement.scale(max(0.0, 1 - DRAG_COEFF * deltaMovement.length() / aspectStack.amount))

        var slipperiness = 1f
        if (onGround) {
            val pos = BlockPos(x, y - 1.0, z)
            slipperiness = level.getBlockState(pos).getSlipperiness(level, pos, this)
        }
        deltaMovement = deltaMovement.multiply(slipperiness.toDouble(), 1.0, slipperiness.toDouble())
        if (onGround) {
            //what is this for?
            //deltaMovement = deltaMovement.multiply(1.0, -0.9, 1.0)
        }


        ++tickCount
        ++age
        if (age >= 20 * 5) {
            remove()
        }
    }

    override fun defineSynchedData() {}

    override fun addAdditionalSaveData(pCompound: CompoundNBT) {
        pCompound.putShort("Age", age.toShort())
        pCompound.put("AspectStack", aspectStack.nbt)
    }

    override fun readAdditionalSaveData(pCompound: CompoundNBT) {
        age = pCompound.getShort("Age").toInt()
        aspectStack = AspectStack.fromNbt(pCompound.get("AspectStack") as CompoundNBT)
    }

    override fun getAddEntityPacket(): IPacket<*> = NetworkHooks.getEntitySpawningPacket(this)

    /**
     * Returns a number from 1 to 10 based on how much aspects this orb is worth. This is used by [arcana.client.render.AspectOrbRenderer] to determine
     * what texture to use.
     */
    @OnlyIn(Dist.CLIENT)
    fun getIcon(): Int {
        return if (aspectStack.amount >= 2477) 10
        else if (aspectStack.amount >= 1237)   9
        else if (aspectStack.amount >= 617)    8
        else if (aspectStack.amount >= 307)    7
        else if (aspectStack.amount >= 149)    6
        else if (aspectStack.amount >= 73)     5
        else if (aspectStack.amount >= 37)     4
        else if (aspectStack.amount >= 17)     3
        else if (aspectStack.amount >= 7)      2
        else if (aspectStack.amount >= 3)      1
        else 0
    }

    override fun writeSpawnData(buffer: PacketBuffer) {
        buffer.writeInt(aspectStack.amount)
    }

    override fun readSpawnData(buffer: PacketBuffer) {
        aspectStack.amount = buffer.readInt()
    }
}