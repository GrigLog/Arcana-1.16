package arcana.common.aspects.graph

import arcana.common.aspects.AspectList
import arcana.common.aspects.graph.nodes.IngredientNode
import arcana.common.aspects.graph.nodes.ItemNode
import arcana.common.aspects.graph.nodes.RecipeNode
import arcana.utils.Pair
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient

class ArcanaGraph {
    var itemNodes: MutableMap<Item, ItemNode> = HashMap()
    var ingNodes: MutableMap<IngredientNode, IngredientNode> =
        HashMap() //it should have been Set but sets in java don't have a "get(key)->key" method
    var recipeNodes: MutableSet<RecipeNode> = HashSet()
    fun addRecipe(recipe: IRecipe<*>, knownItems: Set<Item>) {
        if (recipe.ingredients.isEmpty()) return
        val result = recipe.resultItem
        if (result == ItemStack.EMPTY || knownItems.contains(result.item)) return
        val counts: MutableMap<IngredientNode, Int> = HashMap()
        for (ing in recipe.ingredients) {
            if (ing.isEmpty) continue
            val ingHolder = addIngredient(ing)
            counts.merge(ingHolder.a, ingHolder.b) { a: Int?, b: Int? ->
                Integer.sum(
                    a!!, b!!
                )
            }
        }
        val r = RecipeNode(result, counts)
        r.link = itemNodes[result.item]
        for (ing in r.ingredients) {
            ing!!.links.add(r)
        }
        recipeNodes.add(r)
    }

    private fun addIngredient(ing: Ingredient): Pair<IngredientNode, Int> {
        val nodeNew = IngredientNode(ing)
        val count = nodeNew.reduce()
        var node = ingNodes[nodeNew]
        if (node == null) {
            node = nodeNew
            ingNodes[node] = node
            for (`in` in itemNodes.values) {
                if (node.hasItem(`in`.item)) {
                    `in`.links.add(node)
                }
            }
        }
        return Pair.of(node, count)
    }

    fun addItems(itemAspects: Map<Item, AspectList>, items: Collection<Item>) {
        val remainders: MutableMap<Item, Item> = HashMap()
        for (item in items) {
            val node = ItemNode(item, itemAspects[item])
            itemNodes[item] = node
            if (item.hasCraftingRemainingItem())
                remainders[item] = item.craftingRemainingItem!!
        }
        remainders.forEach { (original: Item, remaining: Item) ->
            val originalNode = itemNodes[original]!!
            val remainingNode = itemNodes[remaining]!!
            remainingNode.origins.add(originalNode)
            originalNode.remainder = remainingNode
        }
    }
}