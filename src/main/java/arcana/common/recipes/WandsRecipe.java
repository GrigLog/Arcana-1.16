package arcana.common.recipes;

import arcana.common.items.ModItems;
import arcana.common.items.spell.CapItem;
import arcana.common.items.spell.CoreItem;
import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class WandsRecipe extends SpecialRecipe {
    private static final List<Integer> SLOTS_1 = ImmutableList.of(1, 2, 3, 5, 6, 7);
    private static final List<Integer> SLOTS_2 = ImmutableList.of(0, 1, 3, 5, 7, 8);
    public WandsRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        if (!canCraftInDimensions(inv.getWidth(), inv.getHeight()))
            return false;
        Item core = inv.getItem(4).getItem();
        if (!isCore(core))
            return false;
        Item cap = inv.getItem(0).getItem();
        if (isCap(cap)
            && cap == inv.getItem(8).getItem()
            && SLOTS_1.stream().allMatch(i -> inv.getItem(i).isEmpty())){
            return ((CoreItem)core).capAllowed((CapItem)cap);
        } else {
            cap = inv.getItem(2).getItem();
            if (isCap(cap)
                && cap == inv.getItem(6).getItem()
                && SLOTS_2.stream().allMatch(i -> inv.getItem(i).isEmpty())) {
                return ((CoreItem) core).capAllowed((CapItem) cap);
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        CapItem cap = (CapItem)(isCap(inv.getItem(0).getItem()) ? inv.getItem(0).getItem() : inv.getItem(2).getItem());
        CoreItem core = (CoreItem)inv.getItem(4).getItem();
        return ModItems.WAND.withCapAndCore(cap,  core, "wand");
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth>=3 && pHeight>=3;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ArcanaRecipes.Serializers.CRAFTING_WANDS;
    }

    private static boolean isCore(Item item){
        return item instanceof CoreItem;
    }

    private static boolean isCap(Item item){
        return item instanceof CapItem;
    }
}
