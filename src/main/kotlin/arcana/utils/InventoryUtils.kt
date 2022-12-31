package arcana.utils

import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.IItemHandler
import java.util.*

object InventoryUtils {
    fun dropContents(world: World, pos: BlockPos, items: IItemHandler) {
        val size = items.slots
        for (i in 0 until size) {
            InventoryHelper.dropItemStack(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), items.getStackInSlot(i))
        }
    }

    fun clear(items: IItemHandler) {
        val size = items.slots
        for (i in 0 until size) {
            items.extractItem(i, items.getStackInSlot(i).count, false)
        }
    }

    fun stacksMatch(a: ItemStack, b: ItemStack): Boolean {
        return a.item === b.item && tagsMatch(a.tag, b.tag)
    }

    fun tagsMatch(a: CompoundNBT?, b: CompoundNBT?): Boolean {
        return Objects.equals(a, b)
    }
}