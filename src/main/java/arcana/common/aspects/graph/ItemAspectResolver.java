package arcana.common.aspects.graph;

import arcana.Arcana;
import arcana.common.aspects.AspectList;
import arcana.common.aspects.graph.nodes.IngredientNode;
import arcana.common.aspects.graph.nodes.ItemNode;
import arcana.common.aspects.graph.nodes.RecipeNode;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;

import java.util.*;
import java.util.stream.Collectors;

public class ItemAspectResolver {
    public static void resolve(Map<Item, AspectList> itemAspects, RecipeManager recipes, Collection<Item> items){
        Arcana.logger.info("Resolving item aspects...");
        Long t1 = System.nanoTime();

        ArcanaGraph graph = new ArcanaGraph();
        for (Item item : items)
            graph.addItem(item, itemAspects.get(item));
        for (IRecipe<?> recipe : recipes.getRecipes())
            graph.addRecipe(recipe, itemAspects.keySet());
        Arcana.logger.info("items: " + graph.itemNodes.size() + ", recipes: " + graph.recipeNodes.size() + ", ingredients:" + graph.ingNodes.size() + ", edges: " + graph.edges);
        resolveAspects(itemAspects, graph);
        Long t2 = System.nanoTime();
        Arcana.logger.info("Done. Took " + (t2 - t1) / 1000000000f + " seconds.");
        Arcana.logger.info(graph.ingNodes.values().toString());
    }

    private static void resolveAspects(Map<Item, AspectList> knownItems, ArcanaGraph graph) {
        List<ItemNode> itemUpdate = knownItems.keySet().stream().map(item -> graph.itemNodes.get(item)).collect(Collectors.toList());

        SortedSet<RecipeNode> knownRecipes = new TreeSet<>(); //recipes with all known ingredients
        Set<RecipeNode> checkKnownRecipes = new HashSet<>(); //unknown recipes which might become known in this update
        Set<RecipeNode> updateRecipes = new HashSet<>(); //only known recipes must go in there!
        while (true){
            for (ItemNode item : itemUpdate) {
                for (IngredientNode ing : item.links) {
                    boolean ingBecameKnown = ing.isUnknown();
                    boolean ingUpdated = ing.updateValue(item);
                    for (RecipeNode r : ing.links){
                        if (r.isUnknown() && ingBecameKnown)
                            checkKnownRecipes.add(r);
                        else if (ingUpdated){
                            updateRecipes.add(r);
                        }
                    }
                }
            }
            for (RecipeNode r : checkKnownRecipes){
                boolean allKnown = true;
                for (IngredientNode ing : r.ingredients){
                    if (ing.isUnknown()) {
                        allKnown = false;
                        break;
                    }
                }
                if (allKnown) {
                    r.updateValue();
                    knownRecipes.add(r);
                }
            }
            if (knownRecipes.size() == 0)
                break;
            checkKnownRecipes.clear();

            updateRecipes.forEach(RecipeNode::updateValue);
            updateRecipes.clear();

            RecipeNode bestRecipe = knownRecipes.first();
            ItemNode newNode = bestRecipe.link;
            knownRecipes.removeIf(r -> r.link.item == newNode.item);
            newNode.cache.update(bestRecipe.getList());
            knownItems.put(newNode.item, newNode.cache.list);
            itemUpdate = ImmutableList.of(newNode);
        }
    }
}
