package arcana.common.blocks.tiles

import arcana.common.blocks.QuickWallBlock.Companion.AGE_2
import arcana.utils.Util.to3d
import net.minecraft.block.Blocks
import net.minecraft.block.DirectionalBlock
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Vector3d

class QuickWallTile(type: TileEntityType<*> = ModTiles.QUICK_WALL) : TileEntity(type), ITickableTileEntity {
    override fun tick() {
        val age = blockState.getValue(AGE_2)
        when (age) {
            0 -> level!!.setBlockAndUpdate(blockPos, blockState.setValue(AGE_2, age + 1))
            1 -> {
                val facing = blockState.getValue(DirectionalBlock.FACING)
                val pos = Vector3d.atLowerCornerOf(blockPos.offset(facing.normal))
                val box = AxisAlignedBB.unitCubeFromLowerCorner(pos)
                for (entity in level!!.getEntities(null, box)) {
                    entity.deltaMovement = facing.normal.to3d()
                }
                level!!.setBlockAndUpdate(blockPos, blockState.setValue(AGE_2, age + 1))
            }
            2 -> level!!.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState())
        }
    }
}