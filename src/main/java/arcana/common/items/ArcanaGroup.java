package arcana.common.items;

import arcana.Arcana;
import arcana.common.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ArcanaGroup extends ItemGroup {
    public ArcanaGroup(){
        super(Arcana.id);
        this.hideTitle();
    }
    @Override public ItemStack makeIcon() {return new ItemStack(ModItems.FIREWAND);}
    @Override public boolean hasSearchBar() {return false;}

    public static Item.Properties itemProps(){
        return new Item.Properties().tab(Arcana.ARCANAGROUP);
    }
}