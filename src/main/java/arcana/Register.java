package arcana;

import arcana.worldgen.ModFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static arcana.worldgen.ModFeatures.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {
    @SubscribeEvent
    static void regStructs(RegistryEvent.Register<Structure<?>> event){
        event.getRegistry().register(tower);
        Structure.STRUCTURES_REGISTRY.put(tower.getRegistryName().toString(), tower);
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Arcana.id, "tower_configured"), ModFeatures.towerConf);
    }
}
