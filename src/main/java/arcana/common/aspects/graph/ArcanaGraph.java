package arcana.common.aspects.graph;

import arcana.common.aspects.AspectList;
import arcana.common.aspects.graph.nodes.IngredientNode;
import arcana.common.aspects.graph.nodes.ItemNode;
import arcana.common.aspects.graph.nodes.RecipeNode;
import arcana.utils.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArcanaGraph {
    public Map<Item, ItemNode> itemNodes = new HashMap<>();
    public Map<IngredientNode, IngredientNode> ingNodes = new HashMap<>(); //it should have been Set but sets in java don't have a "get(key)->key" method
    public Set<RecipeNode> recipeNodes = new HashSet<>();
    int edges = 0; //TODO: remove in future! It's just for debugging
    public ArcanaGraph() {}

    public void addRecipe(IRecipe<?> recipe, Set<Item> knownItems){
        if (recipe.getIngredients().isEmpty())
            return;
        ItemStack result = recipe.getResultItem();
        if (result == ItemStack.EMPTY || knownItems.contains(result.getItem()))
            return;

        Map<IngredientNode, Integer> counts = new HashMap<>();
        for (Ingredient ing : recipe.getIngredients()){
            if (ing.isEmpty())
                continue;
            Pair<IngredientNode, Integer> ingHolder = addIngredient(ing);
            counts.merge(ingHolder.a, ingHolder.b, Integer::sum);
        }

        RecipeNode r = new RecipeNode(result, counts);
        r.link = itemNodes.get(result.getItem());
        edges++;
        for (IngredientNode ing : r.ingredients){
            ing.links.add(r);
            edges++;
        }
        recipeNodes.add(r);
    }

    private Pair<IngredientNode, Integer> addIngredient(Ingredient ing){
        IngredientNode nodeNew = new IngredientNode(ing);
        int count = nodeNew.reduce();
        IngredientNode node = ingNodes.get(nodeNew);
        if (node == null){
            node = nodeNew;
            ingNodes.put(node, node);
            for (ItemNode in : itemNodes.values()){
                if (node.hasItem(in.item)){
                    in.links.add(node);
                    edges++;
                }
            }
        }
        return Pair.of(node, count);
    }

    public void addItem(Item item, AspectList list){
        ItemNode node = new ItemNode(item, list);
        itemNodes.put(item, node);
    }
}
