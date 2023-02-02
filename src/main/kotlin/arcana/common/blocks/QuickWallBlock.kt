package arcana.common.blocks

import arcana.common.blocks.tiles.QuickWallTile
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.DirectionalBlock
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader

class QuickWallBlock(props: Properties = Blocks.STONE.properties) : Block(props) {
    companion object {
        val AGE_2: IntegerProperty = BlockStateProperties.AGE_2
    }
    override fun getShape(state: BlockState, pLevel: IBlockReader, pPos: BlockPos, pContext: ISelectionContext): VoxelShape {
        return if (state.getValue(AGE_2) < 2) VoxelShapes.empty() else VoxelShapes.block()
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(AGE_2, DirectionalBlock.FACING)
    }


    override fun hasTileEntity(state: BlockState) = true

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity {
        return QuickWallTile()
    }
}