package arcana.server.worldgen;

import arcana.common.aspects.Aspects;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModFeatures {
    public static Structure<TowerConfig> tower = new Tower(TowerConfig.CODEC);
    public static StructureFeature<?, ?> towerAir = tower.configured(new TowerConfig(Aspects.AIR));
    public static StructureFeature<?, ?> towerWater = tower.configured(new TowerConfig(Aspects.WATER));
    public static StructureFeature<?, ?> towerEarth = tower.configured(new TowerConfig(Aspects.EARTH));
    public static StructureFeature<?, ?> towerFire = tower.configured(new TowerConfig(Aspects.FIRE));
    public static StructureFeature<?, ?> towerOrder = tower.configured(new TowerConfig(Aspects.ORDER));
    public static StructureFeature<?, ?> towerChaos = tower.configured(new TowerConfig(Aspects.CHAOS));
}
