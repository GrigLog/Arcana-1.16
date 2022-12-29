package arcana.common.capability;

import arcana.common.aspects.Aspects;
import arcana.common.packets.ManaPacket;
import arcana.common.packets.PacketSender;
import arcana.utils.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static arcana.utils.Util.arcLoc;

public class Mana {
    @CapabilityInject(Mana.class)
    public static Capability<Mana> CAPABILITY;
    public static ResourceLocation id = arcLoc("mana");

    public float[] values = new float[Aspects.primal.length];
    public float max;

    public Mana() {
        max = 10;
    }

    public Mana(float[] values, float max) {
        this.values = values;
        this.max = max;
    }

    public void copyTo(Mana other) {
        other.values = values;
        other.max = max;
    }

    public CompoundNBT getNbt() {
        CompoundNBT tag = new CompoundNBT();
        tag.putIntArray("values", Util.farrToIarr(values));
        tag.putFloat("max", max);
        return tag;
    }

    public Mana setNbt(CompoundNBT tag) {
        values = Util.iarrToFarr(tag.getIntArray("values"));
        max = tag.getFloat("max");
        return this;
    }

    public void add(float[] toAdd) {
        for (int i = 0; i < values.length; i++) {
            values[i] = MathHelper.clamp(values[i] + toAdd[i], 0, max);
        }
    }

    public boolean tryConsume(float[] toConsume) {
        boolean enough = true;
        for (int i = 0; i < values.length; i++)
            enough &= values[i] > toConsume[i];
        if (!enough)
            return false;
        for (int i = 0; i < values.length; i++)
            values[i] -= toConsume[i];
        return true;
    }

    public void sendToClient(@Nonnull ServerPlayerEntity player){
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ManaPacket(this));
    }

    public static void ifPresent(LivingEntity entity, Consumer<Mana> func) {
        entity.getCapability(CAPABILITY).resolve().ifPresent(func);
    }

    public static Mana unchecked(LivingEntity entity) {
        return entity.getCapability(CAPABILITY).resolve().get();
    }

    public static class Provider implements ICapabilitySerializable<INBT> {
        private final LazyOptional<Mana> instance = LazyOptional.of(() -> new Mana());

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return cap == CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        public INBT serializeNBT() {
            return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.resolve().get(), null);
        }

        public void deserializeNBT(INBT nbt) {
            CAPABILITY.getStorage().readNBT(CAPABILITY, instance.resolve().get(), null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<Mana> {
        public INBT writeNBT(Capability<Mana> capability, Mana ManaCap, Direction side) {
            return ManaCap.getNbt();
        }

        public void readNBT(Capability<Mana> capability, Mana ManaCap, Direction side, INBT nbt) {
            ManaCap.setNbt((CompoundNBT)nbt);
        }
    }
}
