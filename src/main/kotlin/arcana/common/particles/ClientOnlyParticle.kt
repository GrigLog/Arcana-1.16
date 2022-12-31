package arcana.common.particles

import com.mojang.serialization.Codec
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

abstract class ClientOnlyParticle {
    class Type<T : IParticleData>(ignoreDistance: Boolean) :
        ParticleType<T>(ignoreDistance, null) {

        override fun codec(): Codec<T>? {
            return null
        }
    }

    abstract class Data : IParticleData {
        override fun writeToNetwork(pBuffer: PacketBuffer) {}
        override fun writeToString(): String? {
            return null
        }
    }
}