package arcana.utils;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectStack;
import arcana.common.aspects.Aspects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class Codecs {
    public static final Codec<Aspect> ASPECT = ResourceLocation.CODEC.xmap(Aspects.ASPECTS::get, a -> a.id);
    public static final Codec<AspectStack> ASPECT_STACK = RecordCodecBuilder.create(b -> b.group(
        ASPECT.fieldOf("aspect").forGetter(AspectStack::getAspect),
        Codec.INT.fieldOf("amount").forGetter(as -> as.amount)
    ).apply(b, AspectStack::new));

    public static final Codec<Vector3d> VECTOR_3D_CODEC = RecordCodecBuilder.create(o ->
        o.group(Codec.DOUBLE.fieldOf("x")
                    .forGetter(e -> e.x),
                Codec.DOUBLE.fieldOf("y")
                    .forGetter(e -> e.y),
                Codec.DOUBLE.fieldOf("z")
                    .forGetter(e -> e.z))
            .apply(o, Vector3d::new));

    public static final Codec<ItemStack> ITEM_STACK = RecordCodecBuilder.create((builder) ->
        builder.group(
            Registry.ITEM.fieldOf("id").forGetter(ItemStack::getItem),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount),
            CompoundNBT.CODEC.optionalFieldOf("nbt").forGetter((is) -> Optional.ofNullable(is.getTag()))
        ).apply(builder, (id, count, oTag) -> new ItemStack(id, count, oTag.orElse(null))));


    public static <T> T decodeJson(JsonObject json, boolean compressed, Codec<T> codec) {
        return codec.parse(compressed ? JsonOps.COMPRESSED : JsonOps.INSTANCE, json)
            .getOrThrow(false, Arcana.logger::error);
    }

    public static <T> JsonObject encodeJson(T obj, boolean compressed, Codec<T> codec) {
        return (JsonObject) codec.encodeStart(compressed ? JsonOps.COMPRESSED : JsonOps.INSTANCE, obj)
            .getOrThrow(false, Arcana.logger::error);
    }

    public static <T> T decodeNbt(CompoundNBT tag, Codec<T> codec) {
        return codec.parse(NBTDynamicOps.INSTANCE, tag)
            .getOrThrow(false, Arcana.logger::error);
    }

    public static <T> CompoundNBT encodeNbt(T obj, boolean compressed, Codec<T> codec) {
        return (CompoundNBT) codec.encodeStart(NBTDynamicOps.INSTANCE, obj)
            .getOrThrow(false, Arcana.logger::error);
    }
}
