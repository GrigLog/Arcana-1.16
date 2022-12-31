package arcana.data

import arcana.Arcana
import arcana.common.items.ModItemTags
import arcana.common.items.ModItems
import net.minecraft.data.BlockTagsProvider
import net.minecraft.data.DataGenerator
import net.minecraft.data.ItemTagsProvider
import net.minecraft.item.Item
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.Tags.IOptionalNamedTag
import net.minecraftforge.common.data.ExistingFileHelper

class ItemTagsProvider(gen: DataGenerator, blockTags: BlockTagsProvider, helper: ExistingFileHelper?) :
    ItemTagsProvider(gen, blockTags, Arcana.id, helper) {
    override fun addTags() {
        tag(ModItemTags.SCRIBING_TOOLS).add(ModItems.SCRIBING_TOOLS)
        tag(make("forge:ores/silver"))
        tag(make("forge:ingots/silver"))
        tag(make("forge:ingots/arcanium"))
    }

    companion object {
        private fun make(tag: String): IOptionalNamedTag<Item> {
            return ItemTags.createOptional(ResourceLocation(tag))
        }
    }
}