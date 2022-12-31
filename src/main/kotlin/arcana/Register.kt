package arcana

import arcana.common.aspects.AspectUtils
import arcana.common.blocks.ModBlocks
import arcana.common.blocks.tiles.ModTiles
import arcana.common.capability.Knowledge
import arcana.common.capability.Mana
import arcana.common.capability.Marks
import arcana.common.containers.ModContainers
import arcana.common.entities.ModEntities
import arcana.common.items.ModItems
import arcana.common.packets.PacketSender
import arcana.common.particles.ModParticles
import arcana.common.recipes.ArcanaRecipes
import arcana.server.worldgen.ModFeatures
import arcana.utils.Util
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.particles.ParticleType
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.WorldGenRegistries
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.IForgeRegistry

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object Register {
    @SubscribeEvent
    fun regStructs(event: RegistryEvent.Register<Structure<*>>) {
        for (t in arrayOf(
            ModFeatures.towerAir,
            ModFeatures.towerWater,
            ModFeatures.towerEarth,
            ModFeatures.towerFire,
            ModFeatures.towerOrder,
            ModFeatures.towerChaos
        )) {
            regStructs(event.registry, t)
            regConfStruct(t.registryName!!.path + "_configured", t.configured)
        }
    }

    @SubscribeEvent
    fun regItems(event: RegistryEvent.Register<Item>) {
        Util.getFields<Item>(ModItems::class.java, Item::class.java, null).forEach{ event.registry.register(it) }
        AspectUtils.registerItems(event.registry)
    }

    @SubscribeEvent
    fun regBlocks(event: RegistryEvent.Register<Block>) {
        Util.getFields<Block>(ModBlocks::class.java, Block::class.java, null).forEach{ event.registry.register(it) }
    }

    @SubscribeEvent
    fun regTiles(event: RegistryEvent.Register<TileEntityType<*>>) {
        Util.getFields<TileEntityType<*>>(ModTiles::class.java, TileEntityType::class.java, null).forEach{ event.registry.register(it) }
    }

    @SubscribeEvent
    fun regEntities(event: RegistryEvent.Register<EntityType<*>>) {
        Util.getFields<EntityType<*>>(ModEntities::class.java, EntityType::class.java, null).forEach{ event.registry.register(it) }
    }

    @SubscribeEvent
    fun regContainers(event: RegistryEvent.Register<ContainerType<*>>) {
        Util.getFields<ContainerType<*>>(ModContainers::class.java, ContainerType::class.java, null).forEach{ event.registry.register(it) }
    }

    @SubscribeEvent
    fun regParticles(event: RegistryEvent.Register<ParticleType<*>>) {
        event.registry.registerAll(ModParticles.MARK_TYPE)
    }

    @SubscribeEvent
    fun regSerializers(event: RegistryEvent.Register<IRecipeSerializer<*>>) {
        Util.getFields<IRecipeSerializer<*>>(ArcanaRecipes.Serializers::class.java, IRecipeSerializer::class.java, null).forEach{ event.registry.register(it) }
    }

    @SubscribeEvent
    fun setupCommon(event: FMLCommonSetupEvent) {
        CapabilityManager.INSTANCE.register(Marks::class.java, Marks.Storage(), ::Marks)
        CapabilityManager.INSTANCE.register(Knowledge::class.java, Knowledge.Storage(), ::Knowledge)
        CapabilityManager.INSTANCE.register(Mana::class.java, Mana.Storage(), ::Mana)
        PacketSender.init()
    }

    private fun regStructs(registry: IForgeRegistry<Structure<*>>, vararg structs: Structure<*>) {
        for (struct in structs) {
            registry.register(struct)
            Structure.STRUCTURES_REGISTRY[struct.registryName.toString()] = struct
        }
    }

    private fun regConfStruct(name: String, confStruct: StructureFeature<*, *>) {
        Registry.register(
            WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
            ResourceLocation(Arcana.id, name),
            confStruct
        )
    }
}