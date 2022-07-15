package arcana.common.capability;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectUtils;
import arcana.common.aspects.Aspects;
import arcana.common.packets.MarksPacket;
import arcana.common.packets.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
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

    public static final int MARKS_RANGE = 12; //12 chunk radius

    public List<BlockPos>[] positions;

    public Marks() {
        positions = new ArrayList[AspectUtils.primalAspects.length];
        for (int i = 0; i < AspectUtils.primalAspects.length; i++)
            positions[i] = new ArrayList<>();
    }

    public Marks(List<BlockPos>[] positions){
        this.positions = positions;
    }


    public CompoundNBT getNbt() {
        CompoundNBT tag = new CompoundNBT();
        ListNBT arr = new ListNBT();
        for (int i = 0; i < AspectUtils.primalAspects.length; i++){
            ListNBT list = new ListNBT();
            this.positions[i].forEach(bp -> list.add(LongNBT.valueOf(bp.asLong())));
            arr.add(list);
        }
        tag.put("positions", arr);
        return tag;
    }

    public Marks setNbt(CompoundNBT tag) {
        ListNBT arr = (ListNBT) tag.get("positions");
        List<BlockPos>[] positions = new ArrayList[AspectUtils.primalAspects.length];
        for (int i = 0; i < AspectUtils.primalAspects.length; i++){
            positions[i] = arr.getList(i).stream().map(l -> BlockPos.of(((LongNBT)l).getAsLong())).collect(Collectors.toList());
        }
        this.positions = positions;
        return this;
    }

    protected void sendToClient(@Nonnull ServerPlayerEntity player){
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MarksPacket(this));
    }

    public static void sendToClient(@Nonnull PlayerEntity player){
        Marks cap = player.level.getCapability(Marks.CAPABILITY).resolve().orElse(null);
        BlockPos pos = player.blockPosition();
        Marks toSend = new Marks(new ArrayList[AspectUtils.primalAspects.length]);
        for (int i = 0; i < AspectUtils.primalAspects.length; i++) {
            toSend.positions[i] = cap.positions[i].stream().filter(bp -> //only send the closest marks
                        Math.abs(bp.getX() - pos.getX()) < (MARKS_RANGE << 5) &&
                        Math.abs(bp.getZ() - pos.getZ()) < (MARKS_RANGE << 5))
                .collect(Collectors.toList());
        }
        toSend.sendToClient((ServerPlayerEntity) player);
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

