package arcana.common.aspects

import arcana.Arcana
import arcana.utils.Codecs
import com.google.gson.JsonArray
import com.mojang.serialization.JsonOps
import java.util.function.Consumer
import java.util.stream.Collectors

class AspectList(var list: MutableList<AspectStack> = ArrayList()) 
    : Iterable<AspectStack> {


    fun add(a: Aspect, amount: Int): AspectList {
        for (ass in list) {
            if (ass.aspect === a) {
                ass.amount += amount
                return this
            }
        }
        list.add(AspectStack(a, amount))
        return this
    }

    fun add(a: AspectStack): AspectList {
        for (ass in list) {
            if (ass.aspect === a.aspect) {
                ass.amount += a.amount
                return this
            }
        }
        list.add(a)
        return this
    }

    fun add(aspects: Iterable<AspectStack>): AspectList {
        for (ass in aspects) {
            add(ass)
        }
        return this
    }

    fun subtract(a: AspectStack): AspectList {
        for (ass in list) {
            if (ass.aspect === a.aspect) {
                ass.amount = Math.max(ass.amount - a.amount, 0)
            }
        }
        return this
    }

    fun subtract(aspects: AspectList): AspectList {
        for (ass in aspects) {
            subtract(ass)
        }
        return this
    }

    fun copy() = AspectList(list.map(AspectStack::copy).toMutableList())


    val json: JsonArray
        get() {
            val arr = JsonArray()
            list.forEach(Consumer { ass: AspectStack? ->
                arr.add(
                    Codecs.ASPECT_STACK.encodeStart(
                        JsonOps.INSTANCE,
                        ass
                    ).getOrThrow(false) { message: String? -> Arcana.logger.error(message) })
            })
            return arr
        }

    fun getAmount(a: Aspect): Int {
        for (ass in list) {
            if (ass.aspect === a) return ass.amount
        }
        return 0
    }

    fun sum(): Int {
        var s = 0
        for (ass in list) {
            s += ass.amount
        }
        return s
    }

    fun multiply(a: Float): AspectList {
        list = list.stream()
            .peek { ass -> ass.amount = (ass.amount * a).toInt() }
            .filter { ass -> ass.amount > 0 }
            .collect(Collectors.toList())
        return this
    }

    fun toArray(coeff: Float = 1f): FloatArray {
        val res = FloatArray(Aspects.PRIMAL.size)
        for (ass in list) {
            res[Aspects.PRIMAL.indexOf(ass.aspect)] = ass.amount * coeff
        }
        return res
    }

    override fun iterator(): MutableIterator<AspectStack> {
        return list.iterator()
    }

    override fun toString(): String {
        return "Aspects$list"
    }
}