package arcana.common.blocks.tiles

import arcana.common.blocks.ModBlocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.ChestTileEntity
import net.minecraft.tileentity.IChestLid
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid::class)
class QuantumChestTile(type: TileEntityType<*> = ModTiles.QUANTUM_CHEST) : ChestTileEntity(type) {
    //Mostly copy-paste from EnderChestTileEntity since I could not extend from it (required TileEntityType in the constructor)
    var openness: Float = 0f
    var oOpenness: Float = 0f
    var openCount: Int = 0
    private var tickInterval = 0

    override fun tick() {
        //No idea what this does
        if (++this.tickInterval % 20 * 4 == 0) {
            level!!.blockEvent(this.worldPosition, ModBlocks.QUANTUM_CHEST, 1, this.openCount)
        }

        this.oOpenness = this.openness
        val i = worldPosition.x
        val j = worldPosition.y
        val k = worldPosition.z
        val f = 0.1f
        if (this.openCount > 0 && this.openness == 0.0f) {
            val d0 = i.toDouble() + 0.5
            val d1 = k.toDouble() + 0.5
            level!!.playSound(null as PlayerEntity?, d0, j.toDouble() + 0.5, d1, SoundEvents.ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, level!!.random.nextFloat() * 0.1f + 0.9f)
        }

        if (this.openCount == 0 && this.openness > 0.0f || this.openCount > 0 && this.openness < 1.0f) {
            val f2 = this.openness
            if (this.openCount > 0) {
                this.openness += 0.1f
            } else {
                this.openness -= 0.1f
            }

            if (this.openness > 1.0f) {
                this.openness = 1.0f
            }

            val f1 = 0.5f
            if (this.openness < 0.5f && f2 >= 0.5f) {
                val d3 = i.toDouble() + 0.5
                val d2 = k.toDouble() + 0.5
                level!!.playSound(null as PlayerEntity?, d3, j.toDouble() + 0.5, d2, SoundEvents.ENDER_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, level!!.random.nextFloat() * 0.1f + 0.9f)
            }

            if (this.openness < 0.0f) {
                this.openness = 0.0f
            }
        }
    }

    /**
     * See [Block.eventReceived] for more information. This must return true serverside before it is called
     * clientside.
     */
    override fun triggerEvent(pId: Int, pType: Int): Boolean {
        if (pId == 1) {
            this.openCount = pType
            return true
        } else {
            return super.triggerEvent(pId, pType)
        }
    }

    /**
     * invalidates a tile entity
     */
    override fun setRemoved() {
        this.clearCache()
        super.setRemoved()
    }

    fun startOpen() {
        ++this.openCount
        level!!.blockEvent(this.worldPosition, ModBlocks.QUANTUM_CHEST, 1, this.openCount)
    }

    fun stopOpen() {
        --this.openCount
        level!!.blockEvent(this.worldPosition, ModBlocks.QUANTUM_CHEST, 1, this.openCount)
    }

    override fun stillValid(pPlayer: PlayerEntity): Boolean {
        return if (level!!.getBlockEntity(this.worldPosition) !== this) {
            false
        } else {
            !(pPlayer.distanceToSqr(worldPosition.x.toDouble() + 0.5, worldPosition.y.toDouble() + 0.5, worldPosition.z.toDouble() + 0.5) > 64.0)
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun getOpenNess(pPartialTicks: Float): Float {
        return MathHelper.lerp(pPartialTicks, this.oOpenness, this.openness)
    }
}