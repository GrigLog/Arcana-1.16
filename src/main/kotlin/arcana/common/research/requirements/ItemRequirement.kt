package arcana.common.research.requirements

import arcana.Arcana
import arcana.common.research.Requirement
import arcana.utils.InventoryUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.JsonToNBT
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

class ItemRequirement : Requirement {
    lateinit var stack: ItemStack
    override fun satisfied(player: PlayerEntity): Boolean {
        val count = ItemStackHelper.clearOrCountMatchingItems(player.inventory,
                                                              { InventoryUtils.stacksMatch(stack, it) },
                                                              stack.count, true)
        return count >= stack.count
    }

    override fun take(player: PlayerEntity) {
        ItemStackHelper.clearOrCountMatchingItems(player.inventory,
                                                  { InventoryUtils.stacksMatch(stack, it) }, stack.count, false
        )
    }

    override fun deserialize(data: String) {
        val parts = data.split("*")
        val count = parts[0].toInt()
        val tagStart = parts[1].indexOf('{')
        val id = ResourceLocation(if (tagStart != -1) parts[1].substring(0, tagStart) else parts[1])
        val item = ForgeRegistries.ITEMS.getValue(id)
        if (item == null) {
            Arcana.logger.error("Invalid item requirement: $id")
            throw NullPointerException()
        }
        val tag: CompoundNBT? = if (tagStart != -1) JsonToNBT.parseTag(parts[1].substring(tagStart)) else null
        stack = ItemStack(item, count, tag)
    }
}