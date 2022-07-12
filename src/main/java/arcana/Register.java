package arcana;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static arcana.worldgen.ModFeatures.*;
import static arcana.common.items.ModItems.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {
    @SubscribeEvent
    static void regStructs(RegistryEvent.Register<Structure<?>> event){
        regStructs(event.getRegistry(), tower);

        regConfStruct("tower_air", towerAir);
        regConfStruct("tower_water", towerWater);
        regConfStruct("tower_earth", towerEarth);
        regConfStruct("tower_fire", towerFire);
        regConfStruct("tower_order", towerOrder);
        regConfStruct("tower_chaos", towerChaos);
    }

    @SubscribeEvent
    static void regItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(FIREWAND);
    }



    private static void regStructs(IForgeRegistry<Structure<?>> registry, Structure<?> ... structs){
        for (Structure<?> struct : structs) {
            registry.register(struct);
            Structure.STRUCTURES_REGISTRY.put(struct.getRegistryName().toString(), struct);
        }
    }

    private static void regConfStruct(String name, StructureFeature<?, ?> confStruct){
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Arcana.id, name), confStruct);
    }
}
