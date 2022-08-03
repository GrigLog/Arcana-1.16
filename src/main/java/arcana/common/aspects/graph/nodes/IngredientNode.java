package arcana.common.aspects.graph.nodes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class IngredientNode implements IArcanaNode {
    private final int hash;
    public final ItemStack[] stacks;
    public final Item[] items;
    public AspectListCached cache = new AspectListCached();
    public List<RecipeNode> links = new ArrayList<>();

    public IngredientNode(Ingredient ingredient) {
        stacks = ingredient.getItems();
        Arrays.sort(stacks, (a, b) -> {
            int i1 = Registry.ITEM.getId(a.getItem());
            int i2 = Registry.ITEM.getId(b.getItem());
            int res = Integer.compare(i1, i2);
            if (res != 0)
                return res;
            return Integer.compare(a.getCount(), b.getCount());
        });
        items = new Item[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            items[i] = stacks[i].getItem();
        }
        hash = Objects.hash((Object[]) items);
    }

    public int reduce() {
        int gcd = gcd(stacks);
        for (ItemStack is : stacks) {
            is.setCount(is.getCount() / gcd);
        }
        return gcd;
    }

    public boolean updateValue(ItemNode item) {
        if (item.cache.value < cache.value) {
            cache = item.cache;
            return true;
        }
        return false;
    }

    public boolean hasItem(Item item) {
        return Arrays.binarySearch(items, item, Comparator.comparingInt(Registry.ITEM::getId)) >= 0;
    }

    public boolean isUnknown() {
        return cache.value == Integer.MAX_VALUE;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IngredientNode))
            return false;
        ItemStack[] arr2 = ((IngredientNode) obj).stacks;
        if (stacks.length != arr2.length)
            return false;
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i].getItem() != arr2[i].getItem() || stacks[i].getCount() != arr2[i].getCount())
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ingredient[");
        for (ItemStack is : stacks) {
            sb.append(is.getCount()).append('x').append(is.getItem().getRegistryName()).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]|");
        sb.append(cache.value);
        return sb.toString();
    }

    private static int gcd(int a, int b) {
        if (b == 1) //most Ingredients only use ItemStacks with count = 1, so this check is useful!
            return a;
        int temp;
        while (b > 0) {
            temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int gcd(ItemStack[] input) {
        int result = input[0].getCount();
        for (int i = 1; i < input.length; i++)
            result = gcd(result, input[i].getCount());
        return result;
    }
}
