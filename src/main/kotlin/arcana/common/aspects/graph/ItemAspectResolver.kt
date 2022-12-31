package arcana.common.aspects.graph

import arcana.Arcana
import arcana.common.aspects.AspectList
import arcana.common.aspects.graph.nodes.IngredientNode
import arcana.common.aspects.graph.nodes.ItemNode
import arcana.common.aspects.graph.nodes.RecipeNode
import net.minecraft.item.Item
import net.minecraft.item.crafting.RecipeManager
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.stream.Collectors

object ItemAspectResolver {
    fun resolve(itemAspects: MutableMap<Item, AspectList>, recipes: RecipeManager, items: Collection<Item>) {
        Arcana.logger.info("Resolving item aspects...")
        val t1 = System.nanoTime()
        val graph = ArcanaGraph()
        graph.addItems(itemAspects, items)
        for (recipe in recipes.recipes) graph.addRecipe(recipe, itemAspects.keys)
        Arcana.logger.info("items: " + graph.itemNodes.size + ", recipes: " + graph.recipeNodes.size + ", ingredients:" + graph.ingNodes.size)
        resolveAspects(itemAspects, graph)
        val t2 = System.nanoTime()
        Arcana.logger.info("Done. Took " + (t2 - t1) / 1000000000f + " seconds.")
        //Arcana.logger.info(graph.ingNodes.values().toString());
    }

    private fun resolveAspects(knownItems: MutableMap<Item, AspectList>, graph: ArcanaGraph) {
        val itemUpdate: MutableList<ItemNode>
        val temp: MutableList<ItemNode> = java.util.ArrayList()
        val basicOnly: MutableSet<ItemNode> = HashSet()
        val ingUpdate: MutableMap<IngredientNode, ItemNode> =
            HashMap() //the advantage of using this buffer is tiny, but it exists
        val knownRecipes = TreeSet<RecipeNode>() //recipes with all known ingredients
        val checkKnownRecipes: MutableSet<RecipeNode> =
            HashSet() //unknown recipes which might become known in this update
        val updateRecipes: MutableSet<RecipeNode> = HashSet() //only known recipes must go in there!
        itemUpdate = knownItems.keys.stream().map { item: Item? -> graph.itemNodes[item] }
            .collect(
                Collectors.toCollection(
                    Supplier { ArrayList() })
            )
        itemUpdate.forEach(Consumer { `in`: ItemNode? -> if (!`in`!!.knownCrafts()) basicOnly.add(`in`) })
        itemUpdate.removeIf { `in`: ItemNode? -> !`in`!!.knownCrafts() }
        while (true) {
            for (item in itemUpdate) {
                for (origin in item.origins) {
                    origin.calcCraftsValue() //now .knownCrafts() returns true
                    if (basicOnly.contains(origin)) {
                        basicOnly.remove(origin)
                        temp.add(origin)
                    }
                }
            }
            itemUpdate.addAll(temp)
            temp.clear()
            for (item in itemUpdate) {
                for (ing in item.links) {
                    val old = ingUpdate[ing]
                    val oldValue = old?.cache?.value ?: Int.MAX_VALUE
                    if (item.cache.value < oldValue) {
                        ingUpdate[ing] = item
                    }
                }
            }
            itemUpdate.clear()
            ingUpdate.forEach { (ing: IngredientNode?, item: ItemNode?) ->
                val ingBecameKnown = ing.isUnknown
                val ingUpdated = ing.updateValue(item)
                for (r in ing.links) {
                    if (!r.isValid) continue
                    if (r.isUnknown && ingBecameKnown) checkKnownRecipes.add(r) else if (ingUpdated) {
                        updateRecipes.add(r)
                    }
                }
            }
            ingUpdate.clear()
            for (r in checkKnownRecipes) {
                var allKnown = true
                for (ing in r.ingredients) {
                    if (ing!!.isUnknown) {
                        allKnown = false
                        break
                    }
                }
                if (allKnown) {
                    r.updateValue()
                    knownRecipes.add(r)
                }
            }
            if (knownRecipes.size == 0) break
            checkKnownRecipes.clear()
            for (r in updateRecipes) {
                knownRecipes.remove(r)
                r.updateValue()
                knownRecipes.add(r)
            }
            updateRecipes.clear()
            val bestRecipe = knownRecipes.first()
            val newNode = bestRecipe!!.link
            knownRecipes.removeIf { r: RecipeNode? -> r!!.link!!.item === newNode!!.item }
            newNode!!.calcBasic(bestRecipe.list)
            if (newNode.knownCrafts()) {
                knownItems[newNode.item] = newNode.cache.list
                itemUpdate.add(newNode)
            } else {
                basicOnly.add(newNode)
            }
        }
    }
}