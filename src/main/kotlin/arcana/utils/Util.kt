package arcana.utils

import arcana.Arcana
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3i

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

    fun AxisAlignedBB.closestTo(point: Vector3d) =
        Vector3d(MathHelper.clamp(point.x, minX, maxX),
                 MathHelper.clamp(point.y, minY, maxY),
                 MathHelper.clamp(point.z, minZ, maxZ))

    fun Vector3i.scale(scale: Int) = Vector3i(x * scale, y * scale, z * scale)

    fun Vector3i.to3d() = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    fun Entity.eyePosition() = position().add(0.0, eyeHeight.toDouble(), 0.0)
}