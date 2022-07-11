package arcana.common;

import arcana.Arcana;
import arcana.common.items.ArcanaItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ArcanaGroup extends ItemGroup {
    public ArcanaGroup(){super(Arcana.id);this.hideTitle();}
    @Override public ItemStack makeIcon() {return new ItemStack(ArcanaItems.FIREWAND);}
    @Override public boolean hasSearchBar() {return false;}
}