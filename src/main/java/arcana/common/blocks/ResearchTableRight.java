package arcana.common.blocks;

import arcana.Arcana;
import arcana.common.blocks.tiles.ResearchTable;
import arcana.common.containers.ResearchTableContainer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ResearchTableRight extends AdvancedHorizontalBlock {
    public static final BooleanProperty INK = BooleanProperty.create("ink");

    public ResearchTableRight(Properties props) {
        super(props);
    }
    public ResearchTableRight(){
        super(AbstractBlock.Properties.of(Material.WOOD).strength(3).noOcclusion());
        setRegistryName("research_table_right");
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        super.createBlockStateDefinition(state);
        state.add(INK);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ResearchTable();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction facing = ctx.getHorizontalDirection();
        BlockPos pos2 = ctx.getClickedPos().relative(facing.getCounterClockWise());
        if (!ctx.getLevel().getBlockState(pos2).canBeReplaced(ctx))
            return null;
        return defaultBlockState().setValue(FACING, facing).setValue(INK, false);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.DESTROY;
    }

    @Override
    public void onRemove(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(oldState, world, pos, newState, isMoving);
        if (newState.getBlock() != oldState.getBlock()){
            BlockPos left = pos.relative(oldState.getValue(FACING).getCounterClockWise());
            world.destroyBlock(left, true);
        }
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (world.isClientSide)
            return;
        Direction facing = state.getValue(FACING);
        BlockPos posLeft = pos.relative(facing.getCounterClockWise());
        world.setBlockAndUpdate(posLeft, ModBlocks.RESEARCH_TABLE_LEFT.defaultBlockState().setValue(FACING, facing).setValue(ResearchTableLeft.PAPER, false));
        //world.updateNeighborsAt(posLeft, Blocks.AIR);
        //state.updateNeighbourShapes(world, posLeft, 3);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult pHit) {
        if(!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos);
            ResearchTableContainer.open((ServerPlayerEntity) player, (ResearchTable) te);
        }
        return ActionResultType.SUCCESS;
    }
}
