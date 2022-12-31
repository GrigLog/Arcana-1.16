package arcana.utils

import arcana.Arcana
import arcana.common.aspects.AspectStack
import arcana.common.aspects.Aspects
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTDynamicOps
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.registry.Registry
import java.util.*

object Codecs {
    val ASPECT = ResourceLocation.CODEC.xmap({ rl -> Aspects.ASPECTS[rl]!! }, { a -> a!!.id })

    val ASPECT_STACK = RecordCodecBuilder.create { b: RecordCodecBuilder.Instance<AspectStack> ->
        b.group(
            ASPECT.fieldOf("aspect").forGetter { it.aspect },
            Codec.INT.fieldOf("amount").forGetter { it.amount }
        ).apply(b, ::AspectStack)
    }
    val VECTOR_3D_CODEC = RecordCodecBuilder.create { o: RecordCodecBuilder.Instance<Vector3d> ->
        o.group(
            Codec.DOUBLE.fieldOf("x")
                .forGetter { e: Vector3d -> e.x },
            Codec.DOUBLE.fieldOf("y")
                .forGetter { e: Vector3d -> e.y },
            Codec.DOUBLE.fieldOf("z")
                .forGetter { e: Vector3d -> e.z })
            .apply(o) { pX: Double?, pY: Double?, pZ: Double? ->
                Vector3d(
                    pX!!, pY!!, pZ!!
                )
            }
    }
    val ITEM_STACK = RecordCodecBuilder.create { builder: RecordCodecBuilder.Instance<ItemStack> ->
        builder.group(
            Registry.ITEM.fieldOf("id").forGetter { it.item },
            Codec.INT.optionalFieldOf("count", 1).forGetter { it.count },
            CompoundNBT.CODEC.optionalFieldOf("nbt").forGetter { Optional.ofNullable(it.tag) }
        ).apply(builder) { id, count, oTag -> ItemStack(id, count!!, oTag.orElse(null)) }
    }

    fun <T> decodeJson(json: JsonObject, compressed: Boolean, codec: Codec<T>): T {
        return codec.parse(if (compressed) JsonOps.COMPRESSED else JsonOps.INSTANCE, json)
            .getOrThrow(false, Arcana.logger::error)
    }

    fun <T> encodeJson(obj: T, compressed: Boolean, codec: Codec<T>): JsonObject {
        return codec.encodeStart(if (compressed) JsonOps.COMPRESSED else JsonOps.INSTANCE, obj)
            .getOrThrow(false, Arcana.logger::error) as JsonObject
    }

    fun <T> decodeNbt(tag: CompoundNBT, codec: Codec<T>): T {
        return codec.parse(NBTDynamicOps.INSTANCE, tag)
            .getOrThrow(false, Arcana.logger::error)
    }

    fun <T> encodeNbt(obj: T, compressed: Boolean, codec: Codec<T>): CompoundNBT {
        return codec.encodeStart(NBTDynamicOps.INSTANCE, obj)
            .getOrThrow(false, Arcana.logger::error) as CompoundNBT
    }
}