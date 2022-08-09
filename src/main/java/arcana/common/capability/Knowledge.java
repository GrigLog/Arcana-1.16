package arcana.common.capability;

import arcana.Arcana;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Knowledge {
    @CapabilityInject(Knowledge.class)
    public static Capability<Knowledge> CAPABILITY;
    public static ResourceLocation id = new ResourceLocation(Arcana.id, "knowledge");

    public Set<ResourceLocation> specialRequirementsMet = new HashSet<>();
    public Map<ResourceLocation, Integer> researchProgress = new HashMap<>();

    public Knowledge() {
    }


    public CompoundNBT getNbt() {
        CompoundNBT tag = new CompoundNBT();
        ListNBT specials = new ListNBT();
        for (ResourceLocation rl : specialRequirementsMet)
            specials.add(StringNBT.valueOf(rl.toString()));
        tag.put("specials", specials);
        ListNBT research = new ListNBT();
        researchProgress.forEach((key, progress) -> {
            research.add(StringNBT.valueOf(key.toString() + '/' + progress));
        });
        tag.put("research", research);
        return tag;
    }

    public Knowledge setNbt(CompoundNBT tag) {
        specialRequirementsMet.clear();
        researchProgress.clear();
        for (INBT nbt : tag.getList("specials", Constants.NBT.TAG_STRING)){
            String s = nbt.getAsString();
            specialRequirementsMet.add(new ResourceLocation(s));
        };
        for (INBT nbt : tag.getList("research", Constants.NBT.TAG_STRING)){
            String s = nbt.getAsString();
            int split = s.lastIndexOf('/');
            researchProgress.put(new ResourceLocation(s.substring(0, split)), Integer.parseInt(s.substring(split+1)));
        };
        return this;
    }

    protected void sendToClient(@Nonnull ServerPlayerEntity player){
        //PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new KnowledgePacket(this));
    }

    public static class Provider implements ICapabilitySerializable<INBT> {
        private final LazyOptional<Knowledge> instance = LazyOptional.of(() -> new Knowledge());

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

    public static class Storage implements Capability.IStorage<Knowledge> {
        public INBT writeNBT(Capability<Knowledge> capability, Knowledge KnowledgeCap, Direction side) {
            return KnowledgeCap.getNbt();
        }

        public void readNBT(Capability<Knowledge> capability, Knowledge KnowledgeCap, Direction side, INBT nbt) {
            KnowledgeCap.setNbt((CompoundNBT)nbt);
        }
    }
}
