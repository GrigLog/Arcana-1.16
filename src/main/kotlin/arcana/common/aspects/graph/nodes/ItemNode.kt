package arcana.common.aspects.graph.nodes

import arcana.common.aspects.AspectList
import net.minecraft.item.Item

class ItemNode(val item: Item, list: AspectList?) {
    var cache: AspectListCached
    var links: MutableList<IngredientNode> = ArrayList()
    var cacheCrafts: AspectListCached
    var remainder: ItemNode? =
        null //"bucket" and "milk" are "remainder" and "origin" respectively. One item can have several origins but only one remainder.
    var origins: MutableList<ItemNode> = ArrayList()

    init {
        cache = list?.let { AspectListCached(it) } ?: AspectListCached()
        cacheCrafts = if (item.hasCraftingRemainingItem()) AspectListCached() else cache
    }

    fun calcBasic(list: AspectList) {
        cache.update(list)
    }

    fun calcCraftsValue() {
        cacheCrafts.update(cache.list.copy().subtract(remainder!!.cache.list))
    }

    fun knownBasic(): Boolean {
        return cache.value != Int.MAX_VALUE
    }

    fun knownCrafts(): Boolean {
        return cacheCrafts.value != Int.MAX_VALUE
    }

    override fun toString(): String {
        return "Item{" + item.registryName + '}'
    }
}