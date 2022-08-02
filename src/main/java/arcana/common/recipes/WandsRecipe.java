package arcana.common.recipes;

import arcana.common.items.ModItems;
import arcana.common.items.spell.CapItem;
import arcana.common.items.spell.CoreItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WandsRecipe extends SpecialRecipe {


    public WandsRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        if(inv.getWidth()>=3 && inv.getWidth()>=3){
            ItemStack TL = inv.getItem(0);
            System.out.println(TL.getDisplayName());
            if(isCap(TL)){
                if(TL.sameItem(inv.getItem(8)) &&
                        !(inv.getItem(1)!=ItemStack.EMPTY||
                        inv.getItem(2)!=ItemStack.EMPTY||
                        inv.getItem(3)!=ItemStack.EMPTY||
                        inv.getItem(5)!=ItemStack.EMPTY||
                        inv.getItem(6)!=ItemStack.EMPTY||
                        inv.getItem(7)!=ItemStack.EMPTY))
                    return isCore(inv.getItem(4)) && ((CoreItem)inv.getItem(4).getItem()).capAllowed((CapItem)TL.getItem());
            }else{
                ItemStack stack = inv.getItem(2);
                if(isCap(stack))
                    if(stack.sameItem(inv.getItem(6))&&
                            !(inv.getItem(0)!=ItemStack.EMPTY||
                                    inv.getItem(1)!=ItemStack.EMPTY||
                                    inv.getItem(3)!=ItemStack.EMPTY||
                                    inv.getItem(5)!=ItemStack.EMPTY||
                                    inv.getItem(7)!=ItemStack.EMPTY||
                                    inv.getItem(8)!=ItemStack.EMPTY))
                        return isCore(inv.getItem(4)) && ((CoreItem)inv.getItem(4).getItem()).capAllowed((CapItem)stack.getItem());
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        CapItem caps;
        if(inv.getItem(0).getItem() instanceof CapItem)
            caps = (CapItem)inv.getItem(0).getItem();
        else
            caps = (CapItem)inv.getItem(2).getItem();
        CoreItem item = (CoreItem)inv.getItem(4).getItem();
        return ModItems.WAND.withCapAndCore(caps,  item, "wand");
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth>=3 && pHeight>=3;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ArcanaRecipes.Serializers.CRAFTING_WANDS.get();
    }

    private static boolean isCore(@Nullable ItemStack stack){
        return stack != null && (stack.getItem() instanceof CoreItem);
    }

    private static boolean isCap(@Nullable ItemStack stack){
        return stack != null && (stack.getItem() instanceof CapItem);
    }
}
