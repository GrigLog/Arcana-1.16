package arcana.common.blocks

import arcana.common.blocks.tiles.InfusionMatrixTileEntity
import arcana.common.blocks.tiles.InfusionMatrixTileEntity.Companion.RADIUS
import net.arcanamod.blocks.tiles.PedestalTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

class InfustionMatrixBlock(properties: Properties = Properties.of(Material.STONE).strength(3F)) : Block(properties), ITileEntityProvider {

    override fun hasTileEntity(state: BlockState?) = true

    override fun newBlockEntity(level: IBlockReader) = InfusionMatrixTileEntity()

    override fun onPlace(pState: BlockState, pLevel: World, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving)
        val matrix = pLevel.getBlockEntity(pPos) as InfusionMatrixTileEntity
        val area = AxisAlignedBB(pPos).inflate(RADIUS.x.toDouble(), RADIUS.y.toDouble(), RADIUS.z.toDouble())
        for (pedestalPos in BlockPos.betweenClosedStream(area)) {
            val pedestal = pLevel.getBlockEntity(pedestalPos)
            if (pedestal is PedestalTileEntity && pedestal.infusionMatrix == null) {
                pedestal.infusionMatrix = pPos
                matrix.pedestals.add(pedestalPos)
            }
        }
    }

    override fun onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block !== newState.block) {
            val te: TileEntity? = world.getBlockEntity(pos)
            if (te is InfusionMatrixTileEntity) {
                for (pedestalPos in te.pedestals) {
                    val pedestal = world.getBlockEntity(pedestalPos)
                    if (pedestal is PedestalTileEntity) {
                        pedestal.infusionMatrix = null
                    }
                }
            }
            super.onRemove(state, world, pos, newState, isMoving)
        }
    }
}