package net.arcanamod.blocks.tiles

import arcana.common.blocks.tiles.ModTiles
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraftforge.items.ItemStackHandler


class PedestalTileEntity(type: TileEntityType<*> = ModTiles.PEDESTAL) : TileEntity(type) {
    var itemContainer: ItemStackHandler = object : ItemStackHandler(1) {
        override fun onContentsChanged(slot: Int) {
            super.onContentsChanged(slot)
            setChanged()
        }
    }
    var infusionMatrix: BlockPos? = null


    var itemStack: ItemStack
        get() = itemContainer.getStackInSlot(0)
        set(stack) {
            itemContainer.setStackInSlot(0, stack)
        }

    override fun save(pCompound: CompoundNBT): CompoundNBT {
        super.save(pCompound)
        pCompound.put("items", itemContainer.serializeNBT())
        infusionMatrix?.let {
            pCompound.put("infusionMatrix", NBTUtil.writeBlockPos(it))
        }
        return pCompound
    }

    override fun load(state: BlockState, compound: CompoundNBT) {
        super.load(state, compound)
        itemContainer.deserializeNBT(compound.getCompound("items"))
        infusionMatrix = if (compound.contains("infusionMatrix"))
            NBTUtil.readBlockPos(compound.get("infusionMatrix") as CompoundNBT)
            else null
    }

    override fun getUpdateTag(): CompoundNBT {
        return save(CompoundNBT())
    }

    override fun getRenderBoundingBox(): AxisAlignedBB {
        return AxisAlignedBB(blockPos, blockPos.offset(1, 2, 1))
    }
}