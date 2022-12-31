package arcana.common.recipes

import arcana.common.items.ModItems
import arcana.common.items.wand.CapItem
import arcana.common.items.wand.CoreItem
import com.google.common.collect.ImmutableList
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.SpecialRecipe
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

class WandsRecipe(id: ResourceLocation) : SpecialRecipe(id) {
    override fun matches(inv: CraftingInventory, world: World): Boolean {
        if (!canCraftInDimensions(inv.width, inv.height))
            return false
        val core = inv.getItem(4).item
        if (!isCore(core))
            return false
        var cap = inv.getItem(0).item
        if (isCap(cap) && cap === inv.getItem(8).item
                && SLOTS_1.all { inv.getItem(it).isEmpty })
            return (core as CoreItem).capAllowed(cap as CapItem)
        else {
            cap = inv.getItem(2).item
            if (isCap(cap) && cap === inv.getItem(6).item
                    && SLOTS_2.all{ inv.getItem(it).isEmpty })
                return (core as CoreItem).capAllowed(cap as CapItem)
        }
        return false
    }

    override fun assemble(inv: CraftingInventory): ItemStack {
        val cap = (if (isCap(inv.getItem(0).item)) inv.getItem(0).item else inv.getItem(2).item) as CapItem
        val core = inv.getItem(4).item as CoreItem
        return ModItems.WAND.withCapAndCore(cap, core, "wand")
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return pWidth >= 3 && pHeight >= 3
    }

    override fun getSerializer() = ArcanaRecipes.Serializers.CRAFTING_WANDS

    companion object {
        val SLOTS_1: List<Int> = ImmutableList.of(1, 2, 3, 5, 6, 7)
        val SLOTS_2: List<Int> = ImmutableList.of(0, 1, 3, 5, 7, 8)
        fun isCore(item: Item) = item is CoreItem
        fun isCap(item: Item) = item is CapItem
    }
}