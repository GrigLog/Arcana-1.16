package arcana;

import arcana.common.aspects.AspectUtils;
import arcana.common.blocks.ModBlocks;
import arcana.common.blocks.tiles.ModTiles;
import arcana.common.capability.Knowledge;
import arcana.common.capability.Marks;
import arcana.common.containers.ModContainers;
import arcana.common.entities.ModEntities;
import arcana.common.items.ModItems;
import arcana.common.packets.PacketSender;
import arcana.common.particles.ModParticles;
import arcana.common.recipes.ArcanaRecipes;
import arcana.server.worldgen.Tower;
import arcana.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static arcana.common.items.ModItems.*;
import static arcana.server.worldgen.ModFeatures.*;
import static arcana.common.blocks.ModBlocks.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Register {
    @SubscribeEvent
    static void regStructs(RegistryEvent.Register<Structure<?>> event){
        for (Tower t: new Tower[]{towerAir, towerWater, towerEarth, towerFire, towerOrder, towerChaos}){
            regStructs(event.getRegistry(), t);
            regConfStruct(t.getRegistryName().getPath() + "_configured", t.configured);
        }
    }

    @SubscribeEvent
    static void regItems(RegistryEvent.Register<Item> event){
        Util.<Item>getFields(ModItems.class, Item.class, null).forEach(event.getRegistry()::register);
        AspectUtils.registerItems(event.getRegistry());
    }

    @SubscribeEvent
    static void regBlocks(RegistryEvent.Register<Block> event){
        Util.<Block>getFields(ModBlocks.class, Block.class, null).forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void regTiles(RegistryEvent.Register<TileEntityType<?>> event){
        Util.<TileEntityType<?>>getFields(ModTiles.class, TileEntityType.class, null).forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void regEntities(RegistryEvent.Register<EntityType<?>> event) {
        Util.<EntityType<?>>getFields(ModEntities.class, EntityType.class, null).forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void regContainers(RegistryEvent.Register<ContainerType<?>> event){
        Util.<ContainerType<?>>getFields(ModContainers.class, ContainerType.class, null).forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void regParticles(RegistryEvent.Register<ParticleType<?>> event){
        event.getRegistry().registerAll(ModParticles.markType);
    }

    @SubscribeEvent
    static void regSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event){
        Util.<IRecipeSerializer<?>>getFields(ArcanaRecipes.Serializers.class, IRecipeSerializer.class, null).forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void setupCommon(FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(Marks.class, new Marks.Storage(), Marks::new);
        CapabilityManager.INSTANCE.register(Knowledge.class, new Knowledge.Storage(), Knowledge::new);
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
