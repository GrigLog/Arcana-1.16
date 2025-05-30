package arcana.common.blocks.tiles

import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3i

class InfusionMatrixTileEntity(type: TileEntityType<*> = ModTiles.INFUSION_MATRIX) : TileEntity(type) {
    companion object {
        val RADIUS = Vector3i(8, 5, 8)
    }
    var pedestals: MutableList<BlockPos> = mutableListOf()

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        val list = ListNBT()
        for (pedestal in pedestals)
            list.add(NBTUtil.writeBlockPos(pedestal))
        pCompound.put("pedestals", list)
        return super.save(pCompound)
    }

    override fun load(bs: BlockState, compound: CompoundNBT) {
        val list = compound.get("pedestals") as ListNBT
        pedestals = list.map{NBTUtil.readBlockPos(it as CompoundNBT)}.toMutableList()
        super.load(bs, compound)
    }
}