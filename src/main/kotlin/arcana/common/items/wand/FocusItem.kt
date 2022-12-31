package arcana.common.items.wand

import arcana.common.items.ArcanaGroup
import arcana.common.items.ModItems
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation

class FocusItem() : Item(ArcanaGroup.props.stacksTo(1)) {
    init {
    }

    //public ResourceLocation getModelLocation(CompoundNBT nbt){
    //    int style = nbt.getInt("custom_model_data");
    //    return arcLoc("focus_style_"+style);
    //}
    //@Override
    //public boolean verifyTagAfterLoad(CompoundNBT nbt) {
    //    return true;
    //}
    //@Override
    //public boolean shouldOverrideMultiplayerNbt() {
    //    return false;
    //}
    override fun fillItemCategory(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (allowdedIn(group)) {
            for (style in 0..35) {
                val nbt = CompoundNBT()
                val stack = ItemStack(ModItems.FOCUS.item, 1)
                nbt.putInt("style", style)
                stack.tag = nbt
                items.add(stack)
            }
        }
    }

    companion object {
        var FOCI: MutableMap<ResourceLocation, FocusItem> = LinkedHashMap()
    }
}