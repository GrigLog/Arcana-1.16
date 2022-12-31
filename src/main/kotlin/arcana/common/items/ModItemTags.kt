package arcana.common.items

import arcana.utils.Util
import net.minecraft.item.Item
import net.minecraft.tags.ItemTags
import net.minecraftforge.common.Tags.IOptionalNamedTag

object ModItemTags {
    var SCRIBING_TOOLS: IOptionalNamedTag<Item> = arcTag("scribing_tools")

    private fun arcTag(name: String) = ItemTags.createOptional(Util.arcLoc(name))
}