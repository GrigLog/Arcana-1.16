package arcana.common.blocks.tiles

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT


class QuantumChestInventory : Inventory(27) {
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