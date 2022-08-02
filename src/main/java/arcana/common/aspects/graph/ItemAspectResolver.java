package arcana.common.aspects.graph;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectList;
import arcana.common.aspects.Aspects;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.stream.Collectors;

public class ItemAspectResolver {
    public static void resolve(Map<Item, AspectList> itemAspects, RecipeManager recipes, Collection<Item> items){
        Arcana.logger.info("Resolving item aspects...");
        Long t1 = System.nanoTime();

        ArcanaGraph graph = new ArcanaGraph();
        for (Item item : items)
            graph.addItem(item);
        for (IRecipe<?> recipe : recipes.getRecipes())
            graph.addRecipe(recipe);

        Arcana.logger.info(graph);
        resolveAspects(itemAspects, graph);
        Long t2 = System.nanoTime();
        Arcana.logger.info("Done. Took " + (t2 - t1) / 1000000 + " ms.");
    }

    private static void resolveAspects(Map<Item, AspectList> known, ArcanaGraph graph) {
        List<PrimitiveRecipeNode> knownRecipes = new ArrayList<>();
        Set<PrimitiveRecipeNode> toExamine = graph.recipeNodes;
        while (true) {
            for (PrimitiveRecipeNode r : toExamine) {
                if (known.containsKey(r.output.getItem()))
                    continue;
                boolean allInputsKnown = true;
                for (DefaultEdge edge : graph.incomingEdgesOf(r)) {
                    ItemNode node = (ItemNode) graph.getEdgeSource(edge);
                    if (!known.containsKey(node.item)) {
                        allInputsKnown = false;
                        break;
                    }
                }
                if (allInputsKnown) {
                    knownRecipes.add(r);
                }
            }
            if (knownRecipes.size() == 0)
                break;
            knownRecipes.forEach(r -> r.calcValue(known));
            knownRecipes.sort(Comparator.comparingInt(r -> r.value));
            if (knownRecipes.get(knownRecipes.size() - 1).value == 0)
                break;

            PrimitiveRecipeNode bestRecipe = knownRecipes.remove(0);
            Item newItem = bestRecipe.output.getItem();
            Arcana.logger.info("assigned " + bestRecipe.value + " aspects in total to " + newItem.getRegistryName());
            known.put(newItem, bestRecipe.inputAspects.copy().multiply(0.75f));
            knownRecipes = knownRecipes.stream().filter(r -> r.output.getItem() != newItem).collect(Collectors.toList()); //remove recipes with the same input and output items
            toExamine = graph.outgoingEdgesOf(graph.itemNodes.get(newItem)).stream()
                .map(edge -> (PrimitiveRecipeNode) graph.getEdgeTarget(edge)).collect(Collectors.toSet());
        }
    }
}
