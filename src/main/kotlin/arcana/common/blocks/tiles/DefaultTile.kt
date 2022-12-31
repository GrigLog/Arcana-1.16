package arcana.common.blocks.tiles

import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType

open class DefaultTile(type: TileEntityType<*>) : TileEntity(type) {
    override fun getUpdatePacket(): SUpdateTileEntityPacket {
        return SUpdateTileEntityPacket(worldPosition, -1, save(CompoundNBT()))
    }

    override fun onDataPacket(net: NetworkManager, packet: SUpdateTileEntityPacket) {
        load(blockState, packet.tag)
    }

    override fun getUpdateTag(): CompoundNBT {
        return save(super.getUpdateTag())
    }

    override fun handleUpdateTag(state: BlockState, tag: CompoundNBT) {
        super.handleUpdateTag(state, tag)
        load(state, tag)
    }

    fun saveAndSync() {
        setChanged()
        level!!.setBlockAndUpdate(worldPosition, blockState)
    }
}