package arcana.common.blocks

import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.common.containers.ResearchTableContainer
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.material.PushReaction
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.state.BooleanProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

class ResearchTableRight(props: Properties = Properties.of(Material.WOOD).strength(3f).noOcclusion())
    : AdvancedHorizontalBlock(props) {
    init {
        setRegistryName("research_table_right")
    }

    override fun createBlockStateDefinition(state: StateContainer.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(state)
        state.add(INK)
    }

    override fun hasTileEntity(state: BlockState) = true

    override fun createTileEntity(state: BlockState, world: IBlockReader) = ResearchTable()

    override fun getStateForPlacement(ctx: BlockItemUseContext): BlockState? {
        val facing = ctx.horizontalDirection
        val pos2 = ctx.clickedPos.relative(facing.counterClockWise)
        return if (!ctx.level.getBlockState(pos2).canBeReplaced(ctx)) null else defaultBlockState().setValue(
            FACING,
            facing
        ).setValue(
            INK, false
        )
    }

    override fun getPistonPushReaction(bs: BlockState) = PushReaction.DESTROY

    override fun onRemove(oldState: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        super.onRemove(oldState, world, pos, newState, isMoving)
        if (newState.block !== oldState.block) {
            val left = pos.relative(oldState.getValue(FACING).counterClockWise)
            world.destroyBlock(left, true)
        }
    }

    override fun setPlacedBy(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(world, pos, state, placer, stack)
        if (world.isClientSide) return
        val facing = state.getValue(FACING)
        val posLeft = pos.relative(facing.counterClockWise)
        world.setBlockAndUpdate(
            posLeft,
            ModBlocks.RESEARCH_TABLE_LEFT.defaultBlockState().setValue(FACING, facing)
                .setValue(ResearchTableLeft.PAPER, false)
        )
    }

    override fun use(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        pHit: BlockRayTraceResult
    ): ActionResultType {
        if (!world.isClientSide) {
            val te = world.getBlockEntity(pos)
            ResearchTableContainer.open(player as ServerPlayerEntity, te as ResearchTable)
        }
        return ActionResultType.SUCCESS
    }

    companion object {
        val INK = BooleanProperty.create("ink")
    }
}