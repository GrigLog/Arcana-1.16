package net.arcanamod.blocks.bases

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IWaterLoggable
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.WATERLOGGED
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import java.lang.Boolean

/** Provides sensible overrides for IWaterLoggable implementations */
open class WaterloggableBlock(properties: Properties) : Block(properties), IWaterLoggable {
    init {
        registerDefaultState(getStateDefinition().any().setValue(WATERLOGGED, Boolean.FALSE))
    }

    override fun createBlockStateDefinition(pBuilder: StateContainer.Builder<Block, BlockState>) {
        pBuilder.add(WATERLOGGED)
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState {
        return defaultBlockState().setValue(WATERLOGGED, context.level.getFluidState(context.clickedPos).type === Fluids.WATER)
    }

    override fun updateShape(state: BlockState, facing: Direction, facingState: BlockState, world: IWorld, currentPos: BlockPos, facingPos: BlockPos): BlockState {
        if (state.getValue(WATERLOGGED))
            world.liquidTicks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED))
            Fluids.WATER.getSource(false)
        else
            super.getFluidState(state)
    }
}