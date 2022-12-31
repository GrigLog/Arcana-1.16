package arcana.common.aspects.graph.nodes

import arcana.common.aspects.AspectList
import net.minecraft.item.ItemStack
import kotlin.math.pow

class RecipeNode(result: ItemStack, counts: Map<IngredientNode, Int>) : Comparable<RecipeNode> {
    var ingredients: Array<IngredientNode?>
    var ingCounts: IntArray
    var ratio: Float
    var value = Int.MAX_VALUE //no need for cache because AspectList is only calculated once and used immediately
    var link: ItemNode? = null

    init {
        ratio = 1f / result.count
        val c = counts.size
        if (c <= 9) {
            ratio *= RATIOS[c - 1]
        } else {
            ratio = ratio.toDouble().pow((-1 / 3f).toDouble()).toFloat()
        }
        ingredients = arrayOfNulls(counts.size)
        ingCounts = IntArray(counts.size)
        var i = 0
        for ((key, value1) in counts) {
            ingredients[i] = key
            ingCounts[i] = value1
            i++
        }
    }

    fun updateValue(): Boolean {
        var v = 0
        for (i in ingCounts.indices) {
            v += ingredients[i]!!.cache!!.value * ingCounts[i]
        }
        v *= ratio.toInt()
        if (v < value) {
            value = v
            return true
        }
        return false
    }

    val isUnknown: Boolean
        get() = value == Int.MAX_VALUE
    val isValid: Boolean
        get() = !link!!.knownBasic()

    //Arcana.logger.info("assigned " + value + " aspects in total to " + link.item.getRegistryName());
    val list: AspectList
        get() {
            val list = AspectList()
            for (i in ingCounts.indices) {
                list.add(ingredients[i]!!.cache!!.list.copy().multiply(ingCounts[i].toFloat()))
            }
            //Arcana.logger.info("assigned " + value + " aspects in total to " + link.item.getRegistryName());
            return list.multiply(ratio)
        }

    override fun toString(): String {
        return "Recipe{(" + ingredients.size + ")->" + link!!.item.registryName + '|' + value + '}'
    }

    override fun compareTo(other: RecipeNode): Int {
        val res = value - other.value
        return if (res != 0) res else 1 //returning 0 is unacceptable because TreeSet deletes compareTo-equal objects. I just hope performance will not drop from returning 1's...
    }

    companion object {
        private val RATIOS = floatArrayOf(1f, 0.8f, 0.7f, 0.63f, 0.585f, 0.55f, 0.52f, 0.5f, 0.48f)
    }
}