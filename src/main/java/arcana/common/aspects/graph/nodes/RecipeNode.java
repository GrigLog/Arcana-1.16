package arcana.common.aspects.graph.nodes;

import arcana.Arcana;
import arcana.common.aspects.AspectList;
import net.minecraft.item.ItemStack;

import java.util.*;

public class RecipeNode implements Comparable<RecipeNode> {
    public IngredientNode[] ingredients;
    public int[] ingCounts;
    float ratio;
    public int value = Integer.MAX_VALUE; //no need for cache because AspectList is only calculated once and used immediately
    public ItemNode link;

    private static final float[] RATIOS = new float[]{1, 0.8f, 0.7f, 0.63f, 0.585f, 0.55f, 0.52f, 0.5f, 0.48f};

    public RecipeNode(ItemStack result, Map<IngredientNode, Integer> counts) {
        ratio = 1f / result.getCount();
        int c = counts.size();
        if (c <= 9){
            ratio *= RATIOS[c - 1];
        } else {
            ratio = (float) Math.pow(ratio, -1/3f);
        }
        ingredients = new IngredientNode[counts.size()];
        ingCounts = new int[counts.size()];
        int i = 0;
        for (Map.Entry<IngredientNode, Integer> e : counts.entrySet()) {
            ingredients[i] = e.getKey();
            ingCounts[i] = e.getValue();
            i++;
        }
    }

    public boolean updateValue() {
        int v = 0;
        for (int i = 0; i < ingCounts.length; i++) {
            v += ingredients[i].cache.value * ingCounts[i];
        }
        v *= ratio;
        if (v < value) {
            value = v;
            return true;
        }
        return false;
    }

    public boolean isUnknown() {
        return value == Integer.MAX_VALUE;
    }

    public boolean isValid(){
        return !link.knownBasic();
    }

    public AspectList getList() {
        AspectList list = new AspectList();
        for (int i = 0; i < ingCounts.length; i++) {
            list.add(ingredients[i].cache.list.copy().multiply(ingCounts[i]));
        }
        Arcana.logger.info("assigned " + value + " aspects in total to " + link.item.getRegistryName());
        return list.multiply(ratio);
    }

    @Override
    public String toString() {
        return "Recipe{(" + ingredients.length + ")->" + link.item.getRegistryName() + '|' + value + '}';
    }

    @Override
    public int compareTo(RecipeNode o) {
        int res = value - o.value;
        return res != 0 ? res : 1; //returning 0 is unacceptable because TreeSet deletes compareTo-equal objects. I just hope performance will not drop from returning 1's...
    }
}
