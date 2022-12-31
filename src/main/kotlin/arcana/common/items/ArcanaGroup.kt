package arcana.common.items

import arcana.Arcana
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object ArcanaGroup : ItemGroup(Arcana.id) {
    init {
        hideTitle()
    }

    override fun makeIcon() = ItemStack(ModItems.FIREWAND)
    
    override fun hasSearchBar() = false

    val props: Item.Properties
        get() = Item.Properties().tab(ArcanaGroup)
}