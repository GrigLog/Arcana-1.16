package arcana.server.events;

import arcana.common.capability.Marks;
import arcana.server.worldgen.ModFeatures;
import arcana.server.worldgen.Tower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
    @SubscribeEvent
    static void loadBiome(BiomeLoadingEvent event){
        RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());
        if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.OVERWORLD)){
            event.getGeneration().addStructureStart(ModFeatures.towerAir);
            event.getGeneration().addStructureStart(ModFeatures.towerWater);
            event.getGeneration().addStructureStart(ModFeatures.towerEarth);
        } else if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.NETHER)){
            event.getGeneration().addStructureStart(ModFeatures.towerFire);
        } else if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.END)){
            event.getGeneration().addStructureStart(ModFeatures.towerOrder);
            event.getGeneration().addStructureStart(ModFeatures.towerChaos);
        }

    }

    @SubscribeEvent
    static void loadWorld(WorldEvent.Load event){
        if (event.getWorld().isClientSide())
            return;
        ServerWorld world = (ServerWorld) event.getWorld();
        RegistryKey<World> dim = world.dimension();
        if(world.getChunkSource().getGenerator() instanceof FlatChunkGenerator
            && dim.equals(World.OVERWORLD))
            return;
        world.getChunkSource().generator.getSettings().structureConfig().put(ModFeatures.tower, Tower.separation);
    }
}
