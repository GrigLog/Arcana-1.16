package arcana.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;

public abstract class ClientOnlyParticle {
    public static class Type<T extends IParticleData> extends ParticleType<T>{
        public Type(boolean ignoreDistance, ResourceLocation regName) {
            super(ignoreDistance, null);
            setRegistryName(regName);
        }

        @Override
        public Codec<T> codec() {
            return null;
        }
    }

    public abstract static class Data implements IParticleData{
        @Override
        public void writeToNetwork(PacketBuffer pBuffer) {
        }

        @Override
        public String writeToString() {
            return null;
        }
    }


}
