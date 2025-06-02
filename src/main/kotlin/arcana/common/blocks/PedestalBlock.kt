package arcana.common.blocks

import arcana.common.aspects.ItemAspectRegistry
import arcana.common.blocks.tiles.InfusionMatrixTileEntity
import arcana.common.blocks.tiles.InfusionMatrixTileEntity.Companion.RADIUS
import arcana.common.entities.AspectOrbEntity
import net.arcanamod.blocks.bases.WaterloggableBlock
import net.arcanamod.blocks.tiles.PedestalTileEntity
import net.minecraft.block.BlockState
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.pathfinding.PathType
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

@Suppress("deprecation")
class PedestalBlock(properties: Properties = Properties.of(Material.STONE).strength(3F))
    : WaterloggableBlock(properties), ITileEntityProvider {
    companion object {
        val SHAPE: VoxelShape = VoxelShapes.or(
            box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
            box(3.0, 12.0, 3.0, 13.0, 16.0, 13.0)).optimize()
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE

    override fun isPathfindable(pState: BlockState, pLevel: IBlockReader, pPos: BlockPos, pType: PathType) = false

    override fun hasTileEntity(state: BlockState) = true

    override fun newBlockEntity(world: IBlockReader) = PedestalTileEntity()

    override fun use(dtate: BlockState, level: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType {
        val playerIS: ItemStack = player.getItemInHand(hand)
        val te: PedestalTileEntity = level.getBlockEntity(pos) as PedestalTileEntity

        if (te.itemStack.isEmpty) {
            if (!playerIS.isEmpty) {
                te.itemStack = playerIS.split(1)
                return ActionResultType.SUCCESS
            }
        } else {
            if (!te.itemStack.isEmpty) {
                val aspects = ItemAspectRegistry[te.itemStack]
                if (!player.addItem(te.itemStack)) {
                    val itementity = ItemEntity(level, player.x, player.y, player.z, te.itemStack)
                    itementity.setNoPickUpDelay()
                    level.addFreshEntity(itementity)
                }
                if (aspects.list.isNotEmpty() && !level.isClientSide) {
                    val aspectStack = aspects.list[0]
                    val aspectOrb = AspectOrbEntity(level, Vector3d.upFromBottomCenterOf (te.blockPos, 1.0), aspectStack, te.infusionMatrix)
                    if (te.infusionMatrix != null) {
                        val matrix = level.getBlockEntity(te.infusionMatrix!!) as? InfusionMatrixTileEntity
                        matrix?.aspectOrbs?.add(aspectOrb)
                    }
                    level.addFreshEntity(aspectOrb)
                }
            }
            te.itemStack = ItemStack.EMPTY
            return ActionResultType.CONSUME
        }
        return ActionResultType.PASS
    }

    override fun onPlace(state: BlockState, level: World, pedestalPos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pedestalPos, oldState, isMoving)
        val area = AxisAlignedBB(pedestalPos).inflate(RADIUS.x.toDouble(), RADIUS.y.toDouble(), RADIUS.z.toDouble())
        for (matrixPos in BlockPos.betweenClosedStream(area)) {
            val matrix = level.getBlockEntity(matrixPos)
            if (matrix is InfusionMatrixTileEntity) {
                val pedestal = level.getBlockEntity(pedestalPos) as PedestalTileEntity
                pedestal.infusionMatrix = matrixPos
                matrix.pedestals.add(pedestalPos)
                break
            }
        }
    }

    override fun onRemove(state: BlockState, level: World, pedestalPos: BlockPos, newState: BlockState, isMoving: Boolean) {
        //Arcana.logger.info("pedestal removed: state=" + state + ", newState=" + newState + ", tileEntity=" + level.getBlockEntity(pedestalPos))
        if (state.block !== newState.block) {
            val pedestal: TileEntity? = level.getBlockEntity(pedestalPos)
            if (pedestal is PedestalTileEntity) {
                InventoryHelper.dropItemStack(level, pedestal.blockPos.x.toDouble(), pedestal.blockPos.y.toDouble(), pedestal.blockPos.z.toDouble(), pedestal.itemStack)
                pedestal.infusionMatrix?.let{
                    val matrix = level.getBlockEntity(it)
                    if (matrix is InfusionMatrixTileEntity) {
                        matrix.pedestals.remove(pedestalPos)
                    }
                }
            }
            super.onRemove(state, level, pedestalPos, newState, isMoving)
        }
    }
}