package arcana.common.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.state.StateContainer

open class AdvancedHorizontalBlock(props: Properties) : HorizontalBlock(props) {
    override fun createBlockStateDefinition(state: StateContainer.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(state)
        state.add(FACING)
    }
}