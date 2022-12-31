package arcana.common.aspects.graph.nodes

import arcana.common.aspects.AspectList

class AspectListCached {
    var list = AspectList()
    var value = Int.MAX_VALUE

    constructor() {}
    constructor(aspects: AspectList) {
        update(aspects)
    }

    fun update(aspects: AspectList) {
        list = aspects
        value = list.sum()
    }

    override fun toString(): String {
        return "$list|$value"
    }
}