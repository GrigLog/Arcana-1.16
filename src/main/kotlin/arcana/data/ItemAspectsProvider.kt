package arcana.data

import arcana.Arcana
import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache
import net.minecraft.data.IDataProvider
import net.minecraft.item.DyeColor
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.Tags.IOptionalNamedTag
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class ItemAspectsProvider(var gen: DataGenerator, var id: String = Arcana.id) : IDataProvider {
    var json = JsonObject()
    override fun run(cache: DirectoryCache) {
        val basePath = gen.outputFolder
        val path = basePath.resolve("data/$id/item_aspects/default.json")
        addItemAspects()
        writeIfNecessary(cache, path)
    }

    companion object {
        var GSON = GsonBuilder().setPrettyPrinting().create()
    }

    fun writeIfNecessary(cache: DirectoryCache, path: Path) {
        val file = GSON.toJson(json)
        val hash = IDataProvider.SHA1.hashUnencodedChars(file).toString()
        try {
            if (!Files.exists(path) || cache.getHash(path) != hash) {
                Files.createDirectories(path.parent)
                Files.write(path, file.toByteArray(StandardCharsets.UTF_8))
            }
            cache.putNew(path, hash)
        } catch (exc: IOException) {
            exc.printStackTrace()
        }
    }

    override fun getName() = "$id item aspects"

    fun item(item: Item, list: AspectList) =
        json.add(item.registryName.toString(), list.json)

    fun item(item: String, list: AspectList) =
        json.add(item, list.json)

    fun tag(tag: IOptionalNamedTag<Item>, list: AspectList) =
        json.add('#'.toString() + tag.name.toString(), list.json)

    fun tag(tag: String, list: AspectList) =
        tag(ItemTags.createOptional(ResourceLocation(tag)), list)

    fun addItemAspects() {
        item(Items.BAMBOO, AspectList().add(Aspects.EARTH, 5).add(Aspects.PLANT, 2))
        item(Items.BLAZE_ROD, AspectList().add(Aspects.AURA, 1).add(Aspects.LIGHT, 2).add(Aspects.FIRE, 3).add(Aspects.NETHER, 1))
        item(Items.BONE, AspectList().add(Aspects.DEATH, 2).add(Aspects.BEAST, 1))
        item(Items.BONE_MEAL, AspectList().add(Aspects.DEATH, 1).add(Aspects.LIFE, 1).add(Aspects.EXCHANGE, 1))
        item(Items.BOOK, AspectList().add(Aspects.SENSES, 5).add(Aspects.MIND, 5))
        item(Items.CLAY_BALL, AspectList().add(Aspects.EARTH, 3).add(Aspects.WATER, 3))
        item(Items.COARSE_DIRT, AspectList().add(Aspects.EARTH, 3).add(Aspects.CHAOS, 1))
        item(Items.DARK_PRISMARINE, AspectList().add(Aspects.CRYSTAL, 4).add(Aspects.WATER, 3).add(Aspects.DARKNESS, 2).add(Aspects.MANA, 1))
        item(Items.DIRT, AspectList().add(Aspects.EARTH, 2))
        item(Items.EGG, AspectList().add(Aspects.BEAST, 3).add(Aspects.LIFE, 9))
        item(Items.ENCHANTED_BOOK, AspectList().add(Aspects.SENSES, 5).add(Aspects.MIND, 5))
        item(Items.END_STONE, AspectList().add(Aspects.ENDER, 3).add(Aspects.EARTH, 3).add(Aspects.DARKNESS, 3))
        item(Items.EXPERIENCE_BOTTLE, AspectList().add(Aspects.MANA, 24))
        item(Items.FARMLAND, AspectList().add(Aspects.EARTH, 3).add(Aspects.WATER, 2).add(Aspects.LIFE, 1))
        item(Items.FEATHER, AspectList().add(Aspects.BEAST, 1).add(Aspects.FLIGHT, 4))
        item(Items.FLINT, AspectList().add(Aspects.EARTH, 2).add(Aspects.TOOL, 1))
        item(Items.GLOWSTONE, AspectList().add(Aspects.LIGHT, 8).add(Aspects.SENSES, 4).add(Aspects.NETHER, 1))
        item(Items.GLOWSTONE_DUST, AspectList().add(Aspects.LIGHT, 4).add(Aspects.EARTH, 1).add(Aspects.NETHER, 1))
        item(Items.GRASS_BLOCK, AspectList().add(Aspects.EARTH, 2).add(Aspects.PLANT, 1))
        item(Items.GRASS_PATH, AspectList().add(Aspects.EARTH, 2).add(Aspects.JOURNEY, 1))
        item(Items.GUNPOWDER, AspectList().add(Aspects.ENERGY, 10).add(Aspects.FIRE, 3))
        item(Items.HEART_OF_THE_SEA, AspectList().add(Aspects.LIFE, 5).add(Aspects.AURA, 5).add(Aspects.WATER, 20))
        item(Items.HONEYCOMB, AspectList().add(Aspects.SENSES, 4).add(Aspects.HARVEST, 1))
        item(Items.HONEY_BOTTLE, AspectList().add(Aspects.AIR, 10).add(Aspects.SENSES, 3))
        item(Items.ICE, AspectList().add(Aspects.ICE, 5))
        item(Items.KELP, AspectList().add(Aspects.WATER, 7).add(Aspects.PLANT, 1))
        item(Items.LAPIS_LAZULI, AspectList().add(Aspects.MIND, 3).add(Aspects.CRYSTAL, 1))
        item(Items.LAVA_BUCKET, AspectList().add(Aspects.METAL, 12).add(Aspects.FIRE, 5).add(Aspects.EARTH, 5))
        item(Items.LEATHER, AspectList().add(Aspects.BEAST, 3).add(Aspects.ARMOUR, 3))
        item(Items.MAGMA_BLOCK, AspectList().add(Aspects.FIRE, 3).add(Aspects.OVERWORLD, 1).add(Aspects.NETHER, 1))
        item(Items.MELON, AspectList().add(Aspects.PLANT, 4).add(Aspects.HARVEST, 2))
        item(Items.MILK_BUCKET, AspectList().add(Aspects.METAL, 12).add(Aspects.LIFE, 3).add(Aspects.BEAST, 3))
        item(Items.MYCELIUM, AspectList().add(Aspects.EARTH, 2).add(Aspects.PLANT, 2).add(Aspects.WATER, 1).add(Aspects.EXCHANGE, 1))
        item(Items.NAUTILUS_SHELL, AspectList().add(Aspects.AIR, 5).add(Aspects.WATER, 10).add(Aspects.GREED, 1))
        item(Items.NETHERRACK, AspectList().add(Aspects.FIRE, 2).add(Aspects.EARTH, 1).add(Aspects.NETHER, 1))
        item(Items.QUARTZ, AspectList().add(Aspects.EARTH, 2).add(Aspects.CRYSTAL, 1).add(Aspects.NETHER, 1))
        item(Items.NETHER_QUARTZ_ORE, AspectList().add(Aspects.EARTH, 2).add(Aspects.CRYSTAL, 1).add(Aspects.NETHER, 1))
        item(Items.OBSIDIAN, AspectList().add(Aspects.FIRE, 1).add(Aspects.EARTH, 2).add(Aspects.DARKNESS, 2))
        item(Items.PAPER, AspectList().add(Aspects.PLANT, 1))
        item(Items.PODZOL, AspectList().add(Aspects.EARTH, 2).add(Aspects.PLANT, 1).add(Aspects.METAL, 1))
        item(Items.PRISMARINE, AspectList().add(Aspects.CRYSTAL, 4).add(Aspects.WATER, 3).add(Aspects.MANA, 1))
        item(Items.PRISMARINE_BRICKS, AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.WATER, 4).add(Aspects.MANA, 2))
        item(Items.PUMPKIN, AspectList().add(Aspects.PLANT, 4).add(Aspects.HARVEST, 2))
        item(Items.PURPUR_BLOCK, AspectList().add(Aspects.ENDER, 3).add(Aspects.EARTH, 1).add(Aspects.MANA, 2))
        item(Items.RABBIT_HIDE, AspectList().add(Aspects.BEAST, 1))
        item(Items.SADDLE, AspectList().add(Aspects.JOURNEY, 20).add(Aspects.ARMOUR, 3).add(Aspects.BEAST, 3))
        item(Items.SEA_LANTERN, AspectList().add(Aspects.CRYSTAL, 5).add(Aspects.WATER, 4).add(Aspects.LIGHT, 3).add(Aspects.MANA, 2))
        item(Items.SLIME_BALL, AspectList().add(Aspects.SLIME, 4))
        item(Items.SNOWBALL, AspectList().add(Aspects.ICE, 1))
        item(Items.SOUL_SAND, AspectList().add(Aspects.IMPRISON, 1).add(Aspects.SPIRIT, 1).add(Aspects.NETHER, 1))
        item(Items.STRING, AspectList().add(Aspects.BEAST, 1).add(Aspects.TOOL, 1))
        item(Items.SUGAR_CANE, AspectList().add(Aspects.WATER, 5).add(Aspects.PLANT, 2))
        item(Items.TURTLE_EGG, AspectList().add(Aspects.LIFE, 6).add(Aspects.BEAST, 3).add(Aspects.WATER, 5))
        item(Items.WATER_BUCKET, AspectList().add(Aspects.METAL, 12).add(Aspects.WATER, 5))
        item(Items.WHEAT, AspectList().add(Aspects.PLANT, 6).add(Aspects.HARVEST, 2))
        item(Items.WHEAT_SEEDS, AspectList().add(Aspects.PLANT, 2).add(Aspects.LIFE, 2))
        item(Items.WHITE_WOOL, AspectList().add(Aspects.FABRIC, 4).add(Aspects.BEAST, 1))
        for (a in Aspects.ASPECTS.values) {
            if (a === Aspects.EMPTY) continue
            item("arcana:" + a!!.id.path + "_crystal", AspectList().add(a, 1))
            item("arcana:" + a.id.path, AspectList().add(a, 1))
        }
        item(Items.SHULKER_BOX, AspectList().add(Aspects.VACUUM, 4).add(Aspects.MANA, 2).add(Aspects.ENDER, 1))
        for (c in DyeColor.values()) {
            item(c.getName() + "_shulker_box", AspectList().add(Aspects.VACUUM, 4).add(Aspects.MANA, 2).add(Aspects.ENDER, 1))
        }
        //for (Aspect a : Aspects.primal){
        //    item(a.id.getPath() + "_cluster", new AspectList().add(a, 5));
        //    item(a.id.getPath() + "_cluster_seed", new AspectList().add(a, 1).add(SEEDS, 1).add(CRYSTAL, 1));
        //}
        //item(ModItems.AMBER, new AspectList().add(IMPRISON, 5));
        //item(ModItems.ARCANUM, new AspectList().add(SENSES, 5).add(MIND, 10).add(MANA, 3));
        //item(ModItems.PURIFIED_GOLD, new AspectList().add(METAL, 8).add(GREED, 4));
        //item(ModItems.PURIFIED_IRON, new AspectList().add(METAL, 8));
        //item(ModItems.PURIFIED_SILVER, new AspectList().add(METAL, 8).add(IMPRISON, 4));
        tag("forge:cobblestone", AspectList().add(Aspects.EARTH, 3).add(Aspects.CHAOS, 1))
        tag("forge:dusts/redstone", AspectList().add(Aspects.ENERGY, 3).add(Aspects.MACHINE, 2))
        tag("forge:dyes", AspectList().add(Aspects.SENSES, 5))
        tag("forge:gems/diamond", AspectList().add(Aspects.CRYSTAL, 3).add(Aspects.CREATION, 2))
        tag("forge:gems/emerald", AspectList().add(Aspects.CRYSTAL, 3).add(Aspects.PRIDE, 2))
        tag("forge:gravel", AspectList().add(Aspects.EARTH, 2))
        tag("forge:ingots/arcanium", AspectList().add(Aspects.METAL, 3).add(Aspects.MANA, 3))
        tag("forge:ingots/gold", AspectList().add(Aspects.METAL, 4).add(Aspects.GREED, 2))
        tag("forge:ingots/iron", AspectList().add(Aspects.METAL, 4))
        tag("forge:ingots/silver", AspectList().add(Aspects.METAL, 4).add(Aspects.IMPRISON, 2))
        tag("forge:ores/coal", AspectList().add(Aspects.EARTH, 3).add(Aspects.ENERGY, 2).add(Aspects.FIRE, 1))
        tag("forge:ores/diamond", AspectList().add(Aspects.EARTH, 3).add(Aspects.CRYSTAL, 3).add(Aspects.CREATION, 2))
        tag("forge:ores/emerald", AspectList().add(Aspects.EARTH, 3).add(Aspects.CRYSTAL, 3).add(Aspects.PRIDE, 2))
        tag("forge:ores/gold", AspectList().add(Aspects.EARTH, 3).add(Aspects.METAL, 4).add(Aspects.GREED, 2))
        tag("forge:ores/iron", AspectList().add(Aspects.EARTH, 3).add(Aspects.METAL, 4))
        tag("forge:ores/redstone", AspectList().add(Aspects.EARTH, 3).add(Aspects.ENERGY, 3).add(Aspects.MACHINE, 2))
        tag("forge:sand", AspectList().add(Aspects.EARTH, 3).add(Aspects.AIR, 2))
        tag("forge:sand/red", AspectList().add(Aspects.FIRE, 1))
        tag("forge:stone", AspectList().add(Aspects.EARTH, 4))
        tag("minecraft:banners", AspectList().add(Aspects.FABRIC, 3).add(Aspects.CRAFTING, 3).add(Aspects.SENSES, 2).add(Aspects.PRIDE, 1))
        tag("minecraft:beds", AspectList().add(Aspects.FABRIC, 6).add(Aspects.HUMAN, 4).add(Aspects.CRAFTING, 2).add(Aspects.SLOTH, 1))
        tag("minecraft:boats", AspectList().add(Aspects.TREE, 4).add(Aspects.WATER, 2).add(Aspects.JOURNEY, 1))
        tag("minecraft:buttons", AspectList().add(Aspects.MACHINE, 1))
        tag("minecraft:coals", AspectList().add(Aspects.ENERGY, 2).add(Aspects.FIRE, 1))
        tag("minecraft:leaves", AspectList().add(Aspects.PLANT, 1))
        tag("minecraft:logs", AspectList().add(Aspects.TREE, 4))
        tag("minecraft:planks", AspectList().add(Aspects.TREE, 1))
        tag("minecraft:saplings", AspectList().add(Aspects.PLANT, 2).add(Aspects.TREE, 1).add(Aspects.LIFE, 1))
        tag("minecraft:trapdoors", AspectList().add(Aspects.MACHINE, 2).add(Aspects.MOVEMENT, 1).add(Aspects.IMPRISON, 1))
        tag("minecraft:wooden_trapdoors", AspectList().add(Aspects.TREE, 2))
    }
}