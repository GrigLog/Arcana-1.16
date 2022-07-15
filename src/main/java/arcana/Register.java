package arcana;

import arcana.common.aspects.AspectUtils;
import arcana.common.capability.Marks;
import arcana.common.packets.PacketSender;
import arcana.common.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import static arcana.server.worldgen.ModFeatures.*;
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
        AspectUtils.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    static void regParticles(RegistryEvent.Register<ParticleType<?>> event){
        event.getRegistry().registerAll(ModParticles.markType);
    }

    @SubscribeEvent
    static void setupCommon(FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(Marks.class, new Marks.Storage(), Marks::new);
        PacketSender.init();
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
