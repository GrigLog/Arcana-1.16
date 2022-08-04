package arcana.common.aspects.graph;

import arcana.Arcana;
import arcana.common.aspects.AspectList;
import arcana.common.aspects.graph.nodes.IngredientNode;
import arcana.common.aspects.graph.nodes.ItemNode;
import arcana.common.aspects.graph.nodes.RecipeNode;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;

import java.util.*;
import java.util.stream.Collectors;

public class ItemAspectResolver {
    public static void resolve(Map<Item, AspectList> itemAspects, RecipeManager recipes, Collection<Item> items){
        Arcana.logger.info("Resolving item aspects...");
        Long t1 = System.nanoTime();

        ArcanaGraph graph = new ArcanaGraph();
        graph.addItems(itemAspects, items);
        for (IRecipe<?> recipe : recipes.getRecipes())
            graph.addRecipe(recipe, itemAspects.keySet());
        Arcana.logger.info("items: " + graph.itemNodes.size() + ", recipes: " + graph.recipeNodes.size() + ", ingredients:" + graph.ingNodes.size());
        resolveAspects(itemAspects, graph);
        Long t2 = System.nanoTime();
        Arcana.logger.info("Done. Took " + (t2 - t1) / 1000000000f + " seconds.");
        Arcana.logger.info(graph.ingNodes.values().toString());
    }

    private static void resolveAspects(Map<Item, AspectList> knownItems, ArcanaGraph graph) {
        List<ItemNode> itemUpdate;
        List<ItemNode> temp = new ArrayList<>();
        Set<ItemNode> basicOnly = new HashSet<>();

        Map<IngredientNode, ItemNode> ingUpdate = new HashMap<>(); //the advantage of using this buffer is tiny, but it exists

        TreeSet<RecipeNode> knownRecipes = new TreeSet<>(); //recipes with all known ingredients
        Set<RecipeNode> checkKnownRecipes = new HashSet<>(); //unknown recipes which might become known in this update
        Set<RecipeNode> updateRecipes = new HashSet<>(); //only known recipes must go in there!


        itemUpdate = knownItems.keySet().stream().map(item -> graph.itemNodes.get(item)).collect(Collectors.toCollection(ArrayList::new));
        itemUpdate.forEach(in -> {
            if (!in.knownCrafts())
                basicOnly.add(in);
        });
        itemUpdate.removeIf(in -> !in.knownCrafts());

        while (true){
            for (ItemNode item : itemUpdate){
                for (ItemNode origin : item.origins){
                    origin.calcCraftsValue(); //now .knownCrafts() returns true
                    if (basicOnly.contains(origin)){
                        basicOnly.remove(origin);
                        temp.add(origin);
                    }
                }
            }
            itemUpdate.addAll(temp);
            temp.clear();

            for (ItemNode item : itemUpdate) {
                for (IngredientNode ing : item.links) {
                    ItemNode old = ingUpdate.get(ing);
                    int oldValue = old != null ? old.cache.value : Integer.MAX_VALUE;
                    if (item.cache.value < oldValue){
                        ingUpdate.put(ing, item);
                    }
                }
            }
            itemUpdate.clear();

            ingUpdate.forEach((ing, item) -> {
                boolean ingBecameKnown = ing.isUnknown();
                boolean ingUpdated = ing.updateValue(item);
                for (RecipeNode r : ing.links){
                    if (!r.isValid())
                        continue;
                    if (r.isUnknown() && ingBecameKnown)
                        checkKnownRecipes.add(r);
                    else if (ingUpdated){
                        updateRecipes.add(r);
                    }
                }
            });
            ingUpdate.clear();
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

            for (RecipeNode r : updateRecipes){
                knownRecipes.remove(r);
                r.updateValue();
                knownRecipes.add(r);
            }
            updateRecipes.clear();

            RecipeNode bestRecipe = knownRecipes.first();
            ItemNode newNode = bestRecipe.link;
            knownRecipes.removeIf(r -> r.link.item == newNode.item);
            newNode.calcBasic(bestRecipe.getList());
            if (newNode.knownCrafts()){
                knownItems.put(newNode.item, newNode.cache.list);
                itemUpdate.add(newNode);
            } else {
                basicOnly.add(newNode);
            }
        }
    }
}
