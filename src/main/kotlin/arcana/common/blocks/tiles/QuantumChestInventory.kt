package arcana.common.blocks.tiles

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT


class QuantumChestInventory : Inventory(ROW_SIZE * N_ROWS) {
    companion object {
        val ROW_SIZE = 9
        val N_ROWS = 6
    }

    class Subset(val baseInventory: QuantumChestInventory, val index: Int) : IInventory {
        override fun clearContent() {
            for (i in 0 until ROW_SIZE) {
                setItem(i, ItemStack.EMPTY)
            }
            setChanged()
        }

        override fun getContainerSize(): Int = 9

        override fun isEmpty(): Boolean {
            for (i in 0 until ROW_SIZE) {
                if (!getItem(i).isEmpty)
                    return false
            }
            return true
        }

        override fun getItem(pIndex: Int): ItemStack = baseInventory.getItem(ROW_SIZE * index + pIndex)

        override fun removeItem(pIndex: Int, pCount: Int): ItemStack = baseInventory.removeItem(ROW_SIZE * index + pIndex, pCount)

        override fun removeItemNoUpdate(pIndex: Int): ItemStack = baseInventory.removeItemNoUpdate(ROW_SIZE * index + pIndex)

        override fun setItem(pIndex: Int, pStack: ItemStack) = baseInventory.setItem(ROW_SIZE * index + pIndex, pStack)

        override fun setChanged() = baseInventory.setChanged()

        override fun stillValid(pPlayer: PlayerEntity): Boolean = baseInventory.stillValid(pPlayer)
    }

    fun getSubset(index: Int): Subset = Subset(this, index)

    //Here goes the copy-paste from EnderChestInventory

    var activeChest: QuantumChestTile? = null

    override fun fromTag(pContainerNbt: ListNBT) {
        for (i in 0 until this.containerSize) {
            this.setItem(i, ItemStack.EMPTY)
        }

        for (k in pContainerNbt.indices) {
            val compoundnbt = pContainerNbt.getCompound(k)
            val j = compoundnbt.getByte("Slot").toInt() and 255
            if (j >= 0 && j < this.containerSize) {
                this.setItem(j, ItemStack.of(compoundnbt))
            }
        }
    }

    override fun createTag(): ListNBT {
        val listnbt = ListNBT()

        for (i in 0 until this.containerSize) {
            val itemstack = this.getItem(i)
            if (!itemstack.isEmpty) {
                val compoundnbt = CompoundNBT()
                compoundnbt.putByte("Slot", i.toByte())
                itemstack.save(compoundnbt)
                listnbt.add(compoundnbt)
            }
        }

        return listnbt
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    override fun stillValid(pPlayer: PlayerEntity): Boolean {
        return if (this.activeChest != null && !this.activeChest!!.stillValid(pPlayer)) false else super.stillValid(pPlayer)
    }

    override fun startOpen(pPlayer: PlayerEntity?) {
        if (this.activeChest != null) {
            this.activeChest!!.startOpen()
        }

        super.startOpen(pPlayer)
    }

    override fun stopOpen(pPlayer: PlayerEntity?) {
        if (this.activeChest != null) {
            this.activeChest!!.stopOpen()
        }

        super.stopOpen(pPlayer)
        this.activeChest = null
    }
}