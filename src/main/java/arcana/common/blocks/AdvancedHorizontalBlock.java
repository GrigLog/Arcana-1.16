package arcana.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.StateContainer;

public class AdvancedHorizontalBlock extends HorizontalBlock {
    public AdvancedHorizontalBlock(Properties props) {
        super(props);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        super.createBlockStateDefinition(state);
        state.add(FACING);
    }
}
