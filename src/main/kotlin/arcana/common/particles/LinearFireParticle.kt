package arcana.common.particles

import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.ParticleType

class LinearFireParticle(
    pLevel: ClientWorld,
    pX: Double,
    pY: Double,
    pZ: Double,
    pXSpeed: Double,
    pYSpeed: Double,
    pZSpeed: Double
) : SpriteTexturedParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed) {
    init {
        xd = pXSpeed
        yd = pYSpeed
        zd = pZSpeed
        lifetime = (4.0f / (random.nextFloat() * 0.7f + 0.3f)).toInt() //average is 6.88
    }

    override fun getRenderType() = IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            this.remove()
        } else {
            move(xd, yd, zd)
        }
    }


    class Data() : ClientOnlyParticle.Data() {
        override fun getType(): ParticleType<*> {
            return ModParticles.FIRE
        }
    }

    class Factory(var sprite: IAnimatedSprite) : IParticleFactory<LinearFireParticle.Data> {
        override fun createParticle(
            data: LinearFireParticle.Data,
            pLevel: ClientWorld,
            pX: Double,
            pY: Double,
            pZ: Double,
            pXSpeed: Double,
            pYSpeed: Double,
            pZSpeed: Double
        ): Particle {
            val particle = LinearFireParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed)
            particle.setSpriteFromAge(sprite)
            return particle
        }
    }
}