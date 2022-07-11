package arcana.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class TowerConfig implements IFeatureConfig {
    public static Codec<TowerConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        Codec.INT.fieldOf("data").forGetter(obj -> obj.data)
    ).apply(builder, TowerConfig::new));
    public int data;
    public TowerConfig(int h){
        data = h;
    }
}
