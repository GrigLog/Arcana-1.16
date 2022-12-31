package arcana.common.particles

import arcana.utils.Util.arcLoc
import net.minecraft.particles.ParticleType

object ModParticles {
    @JvmField val MARK_TYPE: ParticleType<MarkParticle.Data> =
        ClientOnlyParticle.Type<MarkParticle.Data>(true).setRegistryName(arcLoc("mark")) as ParticleType<MarkParticle.Data>
}