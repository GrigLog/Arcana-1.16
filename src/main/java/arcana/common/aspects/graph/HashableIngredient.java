package arcana.common.aspects.graph;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Objects;

public class HashableIngredient {
    private final ItemStack[] arr;
    private final int hash;

    public HashableIngredient(Ingredient ingredient){
        arr = ingredient.getItems();
        Arrays.sort(arr, (a, b) -> {
            int i1 = Registry.ITEM.getId(a.getItem());
            int i2 = Registry.ITEM.getId(b.getItem());
            int res = Integer.compare(i1, i2);
            if (res != 0)
                return res;
            return Integer.compare(a.getCount(), b.getCount());
        });
        Item[] items = new Item[arr.length];
        for (int i = 0; i < arr.length; i++){
            items[i] = arr[i].getItem();
        }
        hash = Objects.hash((Object[]) items);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HashableIngredient))
            return false;
        ItemStack[] arr2 = ((HashableIngredient)obj).arr;
        if (arr.length != arr2.length)
            return false;
        for (int i = 0; i < arr.length; i++){
            if (arr[i].getItem() != arr2[i].getItem() || arr[i].getCount() != arr2[i].getCount())
                return false;
        }
        return true;
    }
}
