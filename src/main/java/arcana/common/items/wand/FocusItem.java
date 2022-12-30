package arcana.common.items.wand;

import arcana.Arcana;
import arcana.common.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

import static arcana.utils.Util.arcLoc;

public class FocusItem extends Item{

    public static Map<ResourceLocation, FocusItem> FOCI = new LinkedHashMap<>();
    public ResourceLocation id;

    public FocusItem(ResourceLocation id) {
        super(new Properties().tab(Arcana.ARCANAGROUP).stacksTo(1));
        setRegistryName(id);
        this.id = id;
        FOCI.put(id, this);
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

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(allowdedIn(group)){
            for(int style = 0; style < 36; style++) {
                CompoundNBT nbt = new CompoundNBT();
                ItemStack stack = new ItemStack(ModItems.FOCUS.getItem(), 1);
                nbt.putInt("style", style);
                stack.setTag(nbt);
                items.add(stack);
            }
        }
    }


}
