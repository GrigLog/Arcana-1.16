package arcana.worldgen;

import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModFeatures {
    public static Structure<TowerConfig> tower = new Tower(TowerConfig.CODEC);
    public static StructureFeature<?, ?> towerConf = tower.configured(new TowerConfig(5));
}
