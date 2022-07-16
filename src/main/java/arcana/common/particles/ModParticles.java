package arcana.common.particles;

import arcana.Arcana;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;

public class ModParticles {
    public static ParticleType<MarkParticle.Data> markType = new ClientOnlyParticle.Type<>(true, new ResourceLocation(Arcana.id, "mark"));
}
