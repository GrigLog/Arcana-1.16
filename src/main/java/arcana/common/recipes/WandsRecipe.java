package arcana.common.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WandsRecipe extends SpecialRecipe {


    public WandsRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory pInv, World pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory pInv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }
}
