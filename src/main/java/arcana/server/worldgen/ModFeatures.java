package arcana.server.worldgen;

import arcana.common.aspects.Aspects;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModFeatures {
    public static Tower towerAir = new Tower(Aspects.AIR);
    public static Tower towerWater = new Tower(Aspects.WATER);
    public static Tower towerEarth = new Tower(Aspects.EARTH);
    public static Tower towerFire = new Tower(Aspects.FIRE);
    public static Tower towerOrder = new Tower(Aspects.ORDER);
    public static Tower towerChaos = new Tower(Aspects.CHAOS);

}
