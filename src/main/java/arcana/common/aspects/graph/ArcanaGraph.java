package arcana.common.aspects.graph;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class ArcanaGraph extends SimpleDirectedGraph<IArcanaNode, DefaultEdge> {
    public Map<Item, ItemNode> itemNodes = new HashMap<>();
    public Set<PrimitiveRecipeNode> recipeNodes = new HashSet<>();
    private Map<HashableIngredient, Ingredient> usedIngredients = new HashMap<>();
    public ArcanaGraph() {
        super(DefaultEdge.class);
    }

    public void addRecipe(IRecipe<?> recipe){
        if (recipe.getIngredients().isEmpty())
            return;
        if (recipe.getResultItem() == ItemStack.EMPTY)
            return;
        Map<Ingredient, Integer> counts = new HashMap<>();
        for (Ingredient ing : recipe.getIngredients()){
            if (ing.isEmpty())
                continue;
            counts.merge(intern(ing), 1, Integer::sum);
        }
        List<List<ItemStack>> ings = counts.keySet().stream()
            .map(ing -> Arrays.stream(ing.getItems())
                .map(is -> {
                    ItemStack copy = is.copy();
                    copy.setCount(is.getCount() * counts.get(ing));
                    return copy;})
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
        for (List<ItemStack> inputCombination : Lists.cartesianProduct(ings)){
            PrimitiveRecipeNode n = new PrimitiveRecipeNode(recipe.getResultItem(), inputCombination);
            recipeNodes.add(n);
            addVertex(n);
            for (ItemStack is : inputCombination){
                addEdge(itemNodes.get(is.getItem()), n);
            }
            addEdge(n, itemNodes.get(recipe.getResultItem().getItem()));
        }
    }

    public void addItem(Item item){
        ItemNode n = new ItemNode(item);
        addVertex(n);
        itemNodes.put(item, n);
    }

    //This one helps with merging recipes created by Ingredient.of(stack)
    private Ingredient intern(Ingredient ing){
        HashableIngredient key = new HashableIngredient(ing);
        return usedIngredients.computeIfAbsent(key, k -> ing);
    }
}
