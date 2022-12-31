package arcana.common.blocks

import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.common.containers.ResearchTableContainer
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.material.PushReaction
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.state.BooleanProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.World

class ResearchTableLeft(props: Properties = Properties.of(Material.WOOD).strength(3f).noOcclusion())
    : AdvancedHorizontalBlock(props) {
    init {
        setRegistryName("research_table_left")
    }

    override fun createBlockStateDefinition(state: StateContainer.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(state)
        state.add(PAPER)
    }

    override fun onRemove(oldState: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        super.onRemove(oldState, world, pos, newState, isMoving)
        if (newState.block !== oldState.block) {
            val right = pos.relative(oldState.getValue(FACING).clockWise)
            world.destroyBlock(right, true)
        }
    }

    override fun getPistonPushReaction(bs: BlockState): PushReaction {
        return PushReaction.DESTROY
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
            val te = world.getBlockEntity(pos.relative(state.getValue(FACING).clockWise))
            ResearchTableContainer.open(player as ServerPlayerEntity, te as ResearchTable)
        }
        return ActionResultType.SUCCESS
    }

    companion object {
        val PAPER = BooleanProperty.create("paper")
    }
}