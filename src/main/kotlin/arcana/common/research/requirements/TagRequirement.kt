package arcana.common.research.requirements

import arcana.common.research.Requirement
import arcana.utils.InventoryUtils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.JsonToNBT
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.Tags.IOptionalNamedTag

class TagRequirement : Requirement {
    lateinit var tag: IOptionalNamedTag<Item>
    var count = 0
    var nbt: CompoundNBT? = null
    override fun satisfied(player: PlayerEntity): Boolean {
        val found = ItemStackHelper.clearOrCountMatchingItems(
            player.inventory,
            { stack -> tag.contains(stack.item) && InventoryUtils.tagsMatch(stack.tag, nbt) },
            count, true
        )
        return found >= count
    }

    override fun take(player: PlayerEntity) {
        ItemStackHelper.clearOrCountMatchingItems(
            player.inventory,
            { stack -> tag.contains(stack.item) && InventoryUtils.tagsMatch(stack.tag, nbt) },
            count, false
        )
    }

    override fun deserialize(data: String) {
        val parts = data.split("*")
        count = parts[0].toInt()
        val tagStart = parts[1].indexOf('{')
        tag = ItemTags.createOptional(ResourceLocation(if (tagStart != -1) parts[1].substring(0, tagStart) else parts[1]))
        if (tagStart != -1)
            nbt = JsonToNBT.parseTag(parts[1].substring(tagStart))
    }
}