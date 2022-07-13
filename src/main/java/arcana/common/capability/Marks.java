package arcana.common.capability;

import arcana.Arcana;
import arcana.common.packets.MarksPacket;
import arcana.common.packets.PacketSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Marks {
    @CapabilityInject(Marks.class)
    public static Capability<Marks> CAPABILITY;
    public static ResourceLocation id = new ResourceLocation(Arcana.id, "marks");

    public List<BlockPos> positions;

    public Marks() {
        positions = new ArrayList<>();
    }


    public CompoundNBT getNbt() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLongArray("positions", positions.stream().map(BlockPos::asLong).collect(Collectors.toList()));
        return tag;
    }

    public Marks setNbt(CompoundNBT tag) {
        long[] arr = tag.getLongArray("positions");
        positions = Arrays.stream(arr).mapToObj(BlockPos::of).collect(Collectors.toList());
        return this;
    }

    public void sendToClient(@Nonnull ServerPlayerEntity player){
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MarksPacket(this));
    }

    public static class Provider implements ICapabilitySerializable<INBT> {
        private final LazyOptional<Marks> instance = LazyOptional.of(() -> new Marks());

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

    public static class Storage implements Capability.IStorage<Marks> {
        public INBT writeNBT(Capability<Marks> capability, Marks MarksCap, Direction side) {
            return MarksCap.getNbt();
        }

        public void readNBT(Capability<Marks> capability, Marks MarksCap, Direction side, INBT nbt) {
            MarksCap.setNbt((CompoundNBT)nbt);
        }
    }
}

