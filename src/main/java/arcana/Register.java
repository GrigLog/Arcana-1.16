package arcana;

import arcana.common.aspects.AspectUtils;
import arcana.common.capability.Marks;
import arcana.common.packets.PacketSender;
import arcana.common.particles.ModParticles;
import arcana.server.worldgen.Tower;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
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
        AspectUtils.registerItems(event.getRegistry());

        event.getRegistry().registerAll(FIREWAND);
        event.getRegistry().registerAll(Gauntlet);
        event.getRegistry().registerAll(Focus);

        event.getRegistry().registerAll(AmberCap);
        event.getRegistry().registerAll(BambooCap);
        event.getRegistry().registerAll(ClayCap);
        event.getRegistry().registerAll(EldritchCap);
        event.getRegistry().registerAll(HoneyCap);
        event.getRegistry().registerAll(CopperCap);
        event.getRegistry().registerAll(ElementiumCap);
        event.getRegistry().registerAll(GoldCap);
        event.getRegistry().registerAll(IronCap);
        event.getRegistry().registerAll(ManasteelCap);
        event.getRegistry().registerAll(SilverCap);
        event.getRegistry().registerAll(TerrasteelCap);
        event.getRegistry().registerAll(ThaumiumCap);
        event.getRegistry().registerAll(VoidCap);
        event.getRegistry().registerAll(LeatherCap);
        event.getRegistry().registerAll(MechanicalCap);
        event.getRegistry().registerAll(PrismarineCap);
        event.getRegistry().registerAll(QuartzCap);
        event.getRegistry().registerAll(ShulkerCap);

        event.getRegistry().registerAll(BLAZE_WAND_CORE);
        event.getRegistry().registerAll(ENDROD_WAND_CORE);
        event.getRegistry().registerAll(BONE_WAND_CORE);
        event.getRegistry().registerAll(ICE_WAND_CORE);
        event.getRegistry().registerAll(ARCANE_STONE_WAND_CORE);
        event.getRegistry().registerAll(OBSIDIAN_WAND_CORE);
        event.getRegistry().registerAll(SUGAR_CANE_WAND_CORE);
        event.getRegistry().registerAll(MECHANICAL_WAND_CORE);
        event.getRegistry().registerAll(ELDRITCH_WAND_CORE);
        event.getRegistry().registerAll(CLAY_WAND_CORE);

        event.getRegistry().registerAll(Wand);
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
