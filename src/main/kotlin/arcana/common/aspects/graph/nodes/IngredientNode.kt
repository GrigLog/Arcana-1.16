package arcana.common.aspects.graph.nodes

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.registry.Registry
import java.util.*

class IngredientNode(ingredient: Ingredient) {
    private val hash: Int
    val stacks: Array<ItemStack>
    val items: Array<Item?>
    var cache: AspectListCached? = AspectListCached()
    var links: MutableList<RecipeNode> = ArrayList()

    init {
        stacks = ingredient.items
        Arrays.sort(stacks) { a: ItemStack, b: ItemStack ->
            val i1 = Registry.ITEM.getId(a.item)
            val i2 = Registry.ITEM.getId(b.item)
            val res = Integer.compare(i1, i2)
            if (res != 0) return@sort res
            Integer.compare(a.count, b.count)
        }
        items = arrayOfNulls(stacks.size)
        for (i in stacks.indices) {
            items[i] = stacks[i].item
        }
        hash = Objects.hash(*items as Array<Any?>)
    }

    fun reduce(): Int {
        val gcd = gcd(stacks)
        for (`is` in stacks) {
            `is`.count = `is`.count / gcd
        }
        return gcd
    }

    fun updateValue(item: ItemNode): Boolean {
        if (item.cacheCrafts.value < cache!!.value) {
            cache = item.cacheCrafts
            return true
        }
        return false
    }

    fun hasItem(item: Item?): Boolean {
        return Arrays.binarySearch(
            items,
            item,
            Comparator.comparingInt { pValue: Item? -> Registry.ITEM.getId(pValue) }) >= 0
    }

    val isUnknown: Boolean
        get() = cache!!.value == Int.MAX_VALUE

    override fun hashCode(): Int {
        return hash
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is IngredientNode) return false
        val arr2 = obj.stacks
        if (stacks.size != arr2.size) return false
        for (i in stacks.indices) {
            if (stacks[i].item !== arr2[i].item || stacks[i].count != arr2[i].count) return false
        }
        return true
    }

    override fun toString(): String {
        val sb = StringBuilder("Ingredient[")
        for (`is` in stacks) {
            sb.append(`is`.count).append('x').append(`is`.item.registryName).append(',')
        }
        sb.deleteCharAt(sb.length - 1)
        sb.append("]|")
        sb.append(cache!!.value)
        return sb.toString()
    }

    companion object {
        private fun gcd(a: Int, b: Int): Int {
            var a = a
            var b = b
            if (b == 1) //most Ingredients only use ItemStacks with count = 1, so this check is useful!
                return a
            var temp: Int
            while (b > 0) {
                temp = b
                b = a % b
                a = temp
            }
            return a
        }

        private fun gcd(input: Array<ItemStack>): Int {
            var result = input[0].count
            for (i in 1 until input.size) result = gcd(result, input[i].count)
            return result
        }
    }
}