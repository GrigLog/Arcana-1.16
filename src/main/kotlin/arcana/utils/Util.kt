package arcana.utils

import arcana.Arcana
import net.minecraft.util.ResourceLocation

object Util {
    fun <T> getFields(cls: Class<*>, fieldClass: Class<*>, instance: Any?): List<T> {
        val res: MutableList<T> = ArrayList()
        for (f in cls.declaredFields) {
            try {
                val o = f[instance]
                if (fieldClass.isInstance(o)) res.add(o as T)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return res
    }

    fun arcLoc(s: String): ResourceLocation {
        return ResourceLocation(Arcana.id, s)
    }

    fun <T> fromId(map: Map<ResourceLocation, T>, key: String): T? {
        return map[ResourceLocation(key)]
    }

    fun ResourceLocation.withPath(path: (String)->String) = ResourceLocation(this.namespace, path(this.path))

    fun farrToIarr(arr: FloatArray): IntArray {
        val res = IntArray(arr.size)
        for (i in arr.indices) {
            res[i] = java.lang.Float.floatToIntBits(arr[i])
        }
        return res
    }

    fun iarrToFarr(arr: IntArray): FloatArray {
        val res = FloatArray(arr.size)
        for (i in arr.indices) {
            res[i] = java.lang.Float.intBitsToFloat(arr[i])
        }
        return res
    }

    fun Boolean.toInt() = if (this) 1 else 0
}