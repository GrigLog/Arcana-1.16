package arcana.common.blocks

import net.arcanamod.blocks.bases.WaterloggableBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.pathfinding.PathType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader


class PedestalBlock(properties: Properties = Properties.of(Material.STONE).strength(3F).noOcclusion()) : WaterloggableBlock(properties) {
    companion object {
        val SHAPE: VoxelShape = VoxelShapes.or(
            box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0),
            box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
            box(3.0, 12.0, 3.0, 13.0, 16.0, 13.0)).optimize()
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE

    override fun isPathfindable(pState: BlockState, pLevel: IBlockReader, pPos: BlockPos, pType: PathType) = false

    override fun hasTileEntity(state: BlockState) = false

    //override fun newBlockEntity(world: IBlockReader): TileEntity {
    //    return PedestalTileEntity()
    //}

    /*override fun use(pState: BlockState, pLevel: World, pPos: BlockPos, pPlayer: PlayerEntity, pHand: Hand, pHit: BlockRayTraceResult): ActionResultType {
        val itemstack: ItemStack = player.getHeldItem(hand)
        val te: PedestalTileEntity = world.getTileEntity(pos) as PedestalTileEntity

        if (te.getItem() === ItemStack.EMPTY) {
            if (!itemstack.isEmpty) {
                te.setItem(itemstack.split(1))
                te.markDirty()
                return ActionResultType.SUCCESS
            }
        } else {
            val pedestalItem: ItemStack = te.getItem()
            if (!pedestalItem.isEmpty && !player.addItemStackToInventory(pedestalItem)) {
                val itementity = ItemEntity(world,
                                            player.getPosX(),
                                            player.getPosY(),
                                            player.getPosZ(), pedestalItem)
                itementity.setNoPickupDelay()
                world.addEntity(itementity)
            }
            te.setItem(ItemStack.EMPTY)
            te.markDirty()
            return ActionResultType.CONSUME
        }
        return ActionResultType.PASS
    }*/

    /*override fun onRemove(state: BlockState, world: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block !== newState.block) {
            val te: TileEntity? = world.getBlockEntity(pos)
            if (te is PedestalTileEntity) InventoryHelper.spawnItemStack(world, te.getPos().getX(), te.getPos()
                .getY(), te.getPos().getZ(), (te as PedestalTileEntity).getItem())
            super.onReplaced(state, world, pos, newState, isMoving)
        }
    }*/
}