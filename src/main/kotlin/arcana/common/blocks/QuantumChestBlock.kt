package arcana.common.blocks

import arcana.common.blocks.tiles.ModTiles
import arcana.common.blocks.tiles.QuantumChestTile
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.entity.monster.piglin.PiglinTasks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.container.ChestContainer
import net.minecraft.inventory.container.SimpleNamedContainerProvider
import net.minecraft.item.BlockItemUseContext
import net.minecraft.pathfinding.PathType
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.ChestTileEntity
import net.minecraft.tileentity.EnderChestTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityMerger
import net.minecraft.tileentity.TileEntityMerger.ICallbackWrapper
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.*

class QuantumChestBlock(props: Properties = Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(22.5f, 600.0f).lightLevel { 7 })
    : AbstractChestBlock<QuantumChestTile>(props, {ModTiles.QUANTUM_CHEST}) {
    init {
        registerDefaultState(stateDefinition.any().setValue(EnderChestBlock.FACING, Direction.NORTH).setValue(EnderChestBlock.WATERLOGGED, false))
    }
    companion object {
        val CONTAINER_TITLE: ITextComponent = TranslationTextComponent("container.enderchest")
    }

    override fun hasTileEntity(state: BlockState?): Boolean = true;

    override fun newBlockEntity(world: IBlockReader): TileEntity {
        return QuantumChestTile()
    }

    override fun use(state: BlockState, level: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType {
        //return super.use(state, level, pos, player, hand, hit)
        val enderchestinventory: EnderChestInventory = player.getEnderChestInventory()
        val tileentity: TileEntity? = level.getBlockEntity(pos)
        if (enderchestinventory != null && tileentity is EnderChestTileEntity) {
            val blockpos: BlockPos = pos.above()
            if (level.getBlockState(blockpos).isRedstoneConductor(level, blockpos)) {
                return ActionResultType.sidedSuccess(level.isClientSide)
            } else if (level.isClientSide) {
                return ActionResultType.SUCCESS
            } else {
                enderchestinventory.setActiveChest(tileentity)
                player.openMenu(SimpleNamedContainerProvider({ p_226928_1_: Int, p_226928_2_: PlayerInventory?, p_226928_3_: PlayerEntity? -> ChestContainer.threeRows(p_226928_1_, p_226928_2_, enderchestinventory) }, CONTAINER_TITLE))
                PiglinTasks.angerNearbyPiglins(player, true)
                return ActionResultType.CONSUME
            }
        } else {
            return ActionResultType.sidedSuccess(level.isClientSide)
        }
    }

    //the following methods are copy-pasted from EnderChestBlock.
    // I could not extend it directly because it uses the hardcoded TileEntityType in the constructor.

    /**
     *  essentially the same as [net.minecraft.block.EnderChestBlock.combine] but takes up more space because Kotlin.
     */
    @OnlyIn(Dist.CLIENT)
    override fun combine(pState: BlockState, level: World?, pPos: BlockPos, pOverride: Boolean): ICallbackWrapper<out ChestTileEntity> {
        return object : ICallbackWrapper<ChestTileEntity> {
            override fun <T> apply(callback: TileEntityMerger.ICallback<in ChestTileEntity?, T>): T {
                return callback.acceptNone()
            }
        }
    }

    override fun getShape(pState: BlockState, level: IBlockReader, pPos: BlockPos, pContext: ISelectionContext): VoxelShape
            = box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)

    override fun getRenderShape(pState: BlockState): BlockRenderType {
        return BlockRenderType.ENTITYBLOCK_ANIMATED
    }

    override fun getStateForPlacement(pContext: BlockItemUseContext): BlockState {
        val fluidstate = pContext.level.getFluidState(pContext.clickedPos)
        return defaultBlockState().setValue(EnderChestBlock.FACING, pContext.horizontalDirection.opposite)
            .setValue(EnderChestBlock.WATERLOGGED, fluidstate.type === Fluids.WATER)
    }

    override fun rotate(pState: BlockState, pRotation: Rotation): BlockState {
        return pState.setValue(EnderChestBlock.FACING, pRotation.rotate(pState.getValue(EnderChestBlock.FACING)))
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    override fun mirror(pState: BlockState, pMirror: Mirror): BlockState {
        return pState.rotate(pMirror.getRotation(pState.getValue(EnderChestBlock.FACING)))
    }

    override fun createBlockStateDefinition(pBuilder: StateContainer.Builder<Block?, BlockState?>) {
        pBuilder.add(EnderChestBlock.FACING, EnderChestBlock.WATERLOGGED)
    }

    override fun getFluidState(pState: BlockState): FluidState {
        return if (pState.getValue(EnderChestBlock.WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(pState)
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    override fun updateShape(pState: BlockState, pFacing: Direction?, pFacingState: BlockState?, level: IWorld, pCurrentPos: BlockPos?, pFacingPos: BlockPos?): BlockState {
        if (pState.getValue(EnderChestBlock.WATERLOGGED)) {
            level.liquidTicks.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }

        return super.updateShape(pState, pFacing, pFacingState, level, pCurrentPos, pFacingPos)
    }

    override fun isPathfindable(pState: BlockState?, level: IBlockReader?, pPos: BlockPos?, pType: PathType?): Boolean {
        return false
    }
}