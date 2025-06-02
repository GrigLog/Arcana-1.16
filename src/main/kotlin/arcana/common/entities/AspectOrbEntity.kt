package arcana.common.entities

import arcana.Arcana
import arcana.common.aspects.AspectStack
import arcana.common.aspects.Aspects
import arcana.common.blocks.tiles.InfusionMatrixTileEntity
import arcana.utils.Pair
import arcana.utils.Util.minus
import arcana.utils.Util.plus
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
import kotlin.math.pow

/** [net.minecraft.entity.item.ExperienceOrbEntity] for reference. Also [net.minecraft.entity.projectile.ArrowEntity] */
class AspectOrbEntity(type: EntityType<out AspectOrbEntity>, level: World) : Entity(type, level), IEntityAdditionalSpawnData {
    var aspectStack: AspectStack = AspectStack(Aspects.FIRE, 1)
    var age = 0
    var infusionMatrixPos: BlockPos? = null

    /*companion object {
        fun clientFactory(spawnEntity: FMLPlayMessages.SpawnEntity, level: World): AspectOrbEntity {
            val res = AspectOrbEntity(ModEntities.ASPECT_ORB, level)
            spawnEntity.additionalData
            return res
        }
    }*/

    constructor(level: World, pos: Vector3d, value: AspectStack, infusionMatrixPos: BlockPos?) : this(ModEntities.ASPECT_ORB, level) {
        setPos(pos.x, pos.y, pos.z)
        aspectStack = value
        deltaMovement = Vector3d(0.0, 1.0 / value.amount, 0.0)
        isNoGravity = true  // does not actually do anything since tick() is overriden but whatever
        this.infusionMatrixPos = infusionMatrixPos
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

        move(MoverType.SELF, deltaMovement)
        Arcana.logger.info("${deltaMovement.length()}, isClient=${level.isClientSide}, onGround=$onGround, ")

        val DRAG_COEFF = 0.5
        val GRAVITY_COEFF = 0.5
        val ORB_GRAVITY_COEFF = 0.01

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


        if (infusionMatrixPos != null) {
            val infusionMatrixVec = Vector3d.atCenterOf(infusionMatrixPos!!)
            val matrix = level.getBlockEntity(infusionMatrixPos!!) as? InfusionMatrixTileEntity
            if (matrix != null) {
                val diffMatrix = position() - infusionMatrixVec
                val acceleration = GRAVITY_COEFF / diffMatrix.lengthSqr()
                val accVec = (infusionMatrixVec - position()).normalize().scale(acceleration)
                deltaMovement += accVec

                for (otherOrb in matrix.aspectOrbs) {
                    if (otherOrb == this)
                        continue
                    val compoundAspect = Aspects.getCompound(Pair.of(aspectStack.aspect, otherOrb.aspectStack.aspect))
                    if (compoundAspect == Aspects.EMPTY)
                        continue
                    val diffOrb = otherOrb.position() - position()
                    val accVec = diffOrb.normalize().scale(ORB_GRAVITY_COEFF / diffOrb.lengthSqr() * otherOrb.aspectStack.amount)
                    deltaMovement += accVec
                }

                val WIDTH = 0.1  // should be close to this.bbWidth. But I don't want to access instance members here
                val HEIGHT = 0.1
                val DIST_SQR = ((0.5 + WIDTH / 2).pow(2) * 2 + (0.5 + HEIGHT / 2).pow(2))
                var shouldBeAbsorbed = diffMatrix.lengthSqr() < DIST_SQR
                if (!shouldBeAbsorbed) { //Check if the orb would go through the suction sphere on the next tick. Absorb right now if so.
                    val hSqr = deltaMovement.cross(diffMatrix).lengthSqr() / deltaMovement.lengthSqr()
                    val xSqr = diffMatrix.lengthSqr() - hSqr
                    shouldBeAbsorbed = (position() + deltaMovement).distanceToSqr(infusionMatrixVec) > DIST_SQR && xSqr < deltaMovement.lengthSqr() && hSqr < DIST_SQR
                }
                if (shouldBeAbsorbed) {
                    remove()
                    /*if (!level.isClientSide) {
                        PacketSender.INSTANCE.send(PacketDistributor.NEAR.with {
                            PacketDistributor.TargetPoint(position().x, position().y, position().z, 20.0, level.dimension())
                        }, AspectOrbRemovedPacket(this))
                    }*/
                }
            }
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
        buffer.writeResourceLocation(aspectStack.aspect.id)
        buffer.writeInt(aspectStack.amount)
        buffer.writeBoolean(infusionMatrixPos != null)
        if (infusionMatrixPos != null)
            buffer.writeBlockPos(infusionMatrixPos!!)
    }

    override fun readSpawnData(buffer: PacketBuffer) {
        aspectStack.aspect = Aspects.get(buffer.readResourceLocation())
        aspectStack.amount = buffer.readInt()
        infusionMatrixPos = if (buffer.readBoolean()) buffer.readBlockPos() else null

        //todo: is this the right place?
        infusionMatrixPos?.let{
            val matrix = level.getBlockEntity(it) as? InfusionMatrixTileEntity
            matrix?.let {
                it.aspectOrbs.add(this)
            }
        }
    }
}