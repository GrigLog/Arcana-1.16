package arcana.common.blocks;

import arcana.Arcana;
import arcana.common.blocks.tiles.ResearchTable;
import arcana.common.containers.ResearchTableContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ResearchTableLeft extends AdvancedHorizontalBlock {
    public static final BooleanProperty PAPER = BooleanProperty.create("paper");

    public ResearchTableLeft(Properties props) {
        super(props);
    }
    public ResearchTableLeft(){
        super(AbstractBlock.Properties.of(Material.WOOD).strength(3).noOcclusion());
        setRegistryName("research_table_left");
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        super.createBlockStateDefinition(state);
        state.add(PAPER);
    }

    @Override
    public void onRemove(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(oldState, world, pos, newState, isMoving);
        if (newState.getBlock() != oldState.getBlock()){
            BlockPos right = pos.relative(oldState.getValue(FACING).getClockWise());
            world.destroyBlock(right, true);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult pHit) {
        if(!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise()));
            ResearchTableContainer.open((ServerPlayerEntity) player, (ResearchTable) te);
        }
        return ActionResultType.SUCCESS;
    }

}
