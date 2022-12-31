package arcana.utils

import java.util.*

class Pair<A, B>(var a: A, var b: B) {
    fun flip(): Pair<B, A> {
        return of(b, a)
    }

    operator fun contains(obj: Any): Boolean {
        return a === obj || b === obj
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pair<*, *>) return false
        val pair = other
        return a == pair.a && b == pair.b
    }

    override fun hashCode(): Int {
        return Objects.hash(a, b)
    }

    companion object {
        fun <A, B> of(first: A, second: B): Pair<A, B> {
            return Pair(first, second)
        }
    }
}