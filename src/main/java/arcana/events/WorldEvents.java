package arcana.events;

import arcana.worldgen.ModFeatures;
import arcana.worldgen.Tower;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldEvents {
    @SubscribeEvent
    static void loadBiome(BiomeLoadingEvent event){
        event.getGeneration().addStructureStart(ModFeatures.towerConf);
    }

    @SubscribeEvent
    static void loadWorld(WorldEvent.Load event){
        if (event.getWorld().isClientSide())
            return;
        ServerWorld world = (ServerWorld) event.getWorld();
        if(world.getChunkSource().getGenerator() instanceof FlatChunkGenerator
            && world.dimension().equals(World.OVERWORLD))
            return;
        world.getChunkSource().generator.getSettings().structureConfig().put(ModFeatures.tower, Tower.separation);
    }
}
