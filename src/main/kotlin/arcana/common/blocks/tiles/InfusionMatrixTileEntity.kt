package arcana.common.blocks.tiles

import arcana.Arcana
import arcana.common.entities.AspectOrbEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3i

class InfusionMatrixTileEntity(type: TileEntityType<*> = ModTiles.INFUSION_MATRIX) : TileEntity(type), ITickableTileEntity {
    /*init {
        aspectOrbs = mutableListOf()
    }*/
    companion object {
        val RADIUS = Vector3i(8, 5, 8)
    }
    var pedestals: MutableList<BlockPos> = mutableListOf()
    protected var aspectOrbs: MutableList<AspectOrbEntity> = mutableListOf()
    protected val newAspectOrbs: MutableList<AspectOrbEntity> = mutableListOf()

    fun addOrb(orb: AspectOrbEntity) = newAspectOrbs.add(orb)

    fun getOrbs() : Iterable<AspectOrbEntity> = aspectOrbs

    override fun tick() {
        aspectOrbs = aspectOrbs.filter{it.isAlive}.toMutableList()
        for (orb in newAspectOrbs) {
            if (orb.isAlive)
                aspectOrbs.add(orb)
        }
        newAspectOrbs.clear()
        Arcana.logger.info("aspectOrbs:${aspectOrbs.size}, isClient=${level!!.isClientSide}")
    }

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