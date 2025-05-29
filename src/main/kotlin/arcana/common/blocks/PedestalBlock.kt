package arcana.common.blocks

import arcana.common.aspects.AspectStack
import arcana.common.aspects.Aspects
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

    override fun use(dtate: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType {
        val playerIS: ItemStack = player.getItemInHand(hand)
        val te: PedestalTileEntity = world.getBlockEntity(pos) as PedestalTileEntity

        if (te.itemStack.isEmpty) {
            if (!playerIS.isEmpty) {
                te.itemStack = playerIS.split(1)
                return ActionResultType.SUCCESS
            }
        } else {
            if (!te.itemStack.isEmpty) {
                if (!player.addItem(te.itemStack)) {
                    val itementity = ItemEntity(world, player.x, player.y, player.z, te.itemStack)
                    itementity.setNoPickUpDelay()
                    world.addFreshEntity(itementity)
                }
                val aspectOrb = AspectOrbEntity(world, Vector3d.upFromBottomCenterOf (te.blockPos, 1.0), AspectStack(Aspects.CHAOS, 20))
                world.addFreshEntity(aspectOrb)
            }
            te.itemStack = ItemStack.EMPTY
            return ActionResultType.CONSUME
        }
        return ActionResultType.PASS
    }

    override fun onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block !== newState.block) {
            val te: TileEntity? = world.getBlockEntity(pos)
            if (te is PedestalTileEntity)
                InventoryHelper.dropItemStack(world, te.blockPos.x.toDouble(), te.blockPos.y.toDouble(), te.blockPos.z.toDouble(), te.itemStack)
            super.onRemove(state, world, pos, newState, isMoving)
        }
    }
}