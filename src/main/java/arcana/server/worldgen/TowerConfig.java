package arcana.server.worldgen;

import arcana.common.aspects.Aspect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TowerConfig implements IFeatureConfig {
    public static Codec<TowerConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        ResourceLocation.CODEC.xmap(Aspect::fromId, a -> a.id).fieldOf("aspect").forGetter(config -> config.aspect)
    ).apply(builder, TowerConfig::new));
    public Aspect aspect;
    public TowerConfig(Aspect aspect){
        this.aspect = aspect;
    }
}
