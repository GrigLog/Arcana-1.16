package arcana.data;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectList;
import arcana.common.aspects.AspectUtils;
import arcana.common.items.ModItems;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static arcana.common.aspects.Aspects.*;


public class ItemAspectsProvider implements IDataProvider {
    static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    JsonObject json = new JsonObject();
    DataGenerator gen;
    String id;

    public ItemAspectsProvider(DataGenerator generator, String modid){
        gen = generator;
        id = modid;
    }

    public ItemAspectsProvider(DataGenerator generator){
        this(generator, Arcana.id);
    }

    @Override
    public void run(DirectoryCache cache) throws IOException {
        Path basePath = gen.getOutputFolder();
        Path path = basePath.resolve("data/" + id + "/item_aspects/default.json");
        addItemAspects();
        writeIfNecessary(cache, path);
    }

    void writeIfNecessary(DirectoryCache cache, Path path) {
        String file = GSON.toJson(json);
        String hash = SHA1.hashUnencodedChars(file).toString();
        try {
            if (!Files.exists(path) || !Objects.equals(cache.getHash(path), hash)) {
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes(StandardCharsets.UTF_8));
            }
            cache.putNew(path, hash);
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return id + " item aspects";
    }

    void item(Item item, AspectList list){
        json.add(item.getRegistryName().toString(), list.getJson());
    }

    void item(String item, AspectList list){
        json.add(item, list.getJson());
    }

    void tag(Tags.IOptionalNamedTag<Item> tag, AspectList list){
        json.add('#' + tag.getName().toString(), list.getJson());
    }

    void tag(String tag, AspectList list){
        tag(ItemTags.createOptional(new ResourceLocation(tag)), list);
    }



    void addItemAspects(){
        item(Items.BAMBOO, new AspectList().add(EARTH, 5).add(PLANT, 2));
        item(Items.BLAZE_ROD, new AspectList().add(AURA, 1).add(LIGHT, 2).add(FIRE, 3).add(NETHER, 1));
        item(Items.BONE, new AspectList().add(DEATH, 2).add(BEAST, 1));
        item(Items.BONE_MEAL, new AspectList().add(DEATH, 1).add(LIFE, 1).add(EXCHANGE, 1));
        item(Items.BOOK, new AspectList().add(SENSES, 5).add(MIND, 5));
        item(Items.CLAY_BALL, new AspectList().add(EARTH, 3).add(WATER, 3));
        item(Items.COARSE_DIRT, new AspectList().add(EARTH, 3).add(CHAOS, 1));
        item(Items.DARK_PRISMARINE, new AspectList().add(CRYSTAL, 4).add(WATER, 3).add(DARKNESS, 2).add(MANA, 1));
        item(Items.DIRT, new AspectList().add(EARTH, 2));
        item(Items.EGG, new AspectList().add(BEAST, 3).add(LIFE, 9));
        item(Items.ENCHANTED_BOOK, new AspectList().add(SENSES, 5).add(MIND, 5));
        item(Items.END_STONE, new AspectList().add(ENDER, 3).add(EARTH, 3).add(DARKNESS, 3));
        item(Items.EXPERIENCE_BOTTLE, new AspectList().add(MANA, 24));
        item(Items.FARMLAND, new AspectList().add(EARTH, 3).add(WATER, 2).add(LIFE, 1));
        item(Items.FEATHER, new AspectList().add(BEAST, 1).add(FLIGHT, 4));
        item(Items.FLINT, new AspectList().add(EARTH, 2).add(TOOL, 1));
        item(Items.GLOWSTONE, new AspectList().add(LIGHT, 8).add(SENSES, 4).add(NETHER, 1));
        item(Items.GLOWSTONE_DUST, new AspectList().add(LIGHT, 4).add(EARTH, 1).add(NETHER, 1));
        item(Items.GRASS_BLOCK, new AspectList().add(EARTH, 2).add(PLANT, 1));
        item(Items.GRASS_PATH, new AspectList().add(EARTH, 2).add(JOURNEY, 1));
        item(Items.GUNPOWDER, new AspectList().add(ENERGY, 10).add(FIRE, 3));
        item(Items.HEART_OF_THE_SEA, new AspectList().add(LIFE, 5).add(AURA, 5).add(WATER, 20));
        item(Items.HONEYCOMB, new AspectList().add(SENSES, 4).add(HARVEST, 1));
        item(Items.HONEY_BOTTLE, new AspectList().add(AIR, 10).add(SENSES, 3));
        item(Items.ICE, new AspectList().add(ICE, 5));
        item(Items.KELP, new AspectList().add(WATER, 7).add(PLANT, 1));
        item(Items.LAPIS_LAZULI, new AspectList().add(MIND, 3).add(CRYSTAL, 1));
        item(Items.LAVA_BUCKET, new AspectList().add(METAL, 12).add(FIRE, 5).add(EARTH, 5));
        item(Items.LEATHER, new AspectList().add(BEAST, 3).add(ARMOUR, 3));
        item(Items.MAGMA_BLOCK, new AspectList().add(FIRE, 3).add(OVERWORLD, 1).add(NETHER, 1));
        item(Items.MELON, new AspectList().add(PLANT, 4).add(HARVEST, 2));
        item(Items.MILK_BUCKET, new AspectList().add(METAL, 12).add(LIFE, 3).add(BEAST, 3));
        item(Items.MYCELIUM, new AspectList().add(EARTH, 2).add(PLANT, 2).add(WATER, 1).add(EXCHANGE, 1));
        item(Items.NAUTILUS_SHELL, new AspectList().add(AIR, 5).add(WATER, 10).add(GREED, 1));
        item(Items.NETHERRACK, new AspectList().add(FIRE, 2).add(EARTH, 1).add(NETHER, 1));
        item(Items.QUARTZ, new AspectList().add(EARTH, 2).add(CRYSTAL, 1).add(NETHER, 1));
        item(Items.NETHER_QUARTZ_ORE, new AspectList().add(EARTH, 2).add(CRYSTAL, 1).add(NETHER, 1));
        item(Items.OBSIDIAN, new AspectList().add(FIRE, 1).add(EARTH, 2).add(DARKNESS, 2));
        item(Items.PAPER, new AspectList().add(PLANT, 1));
        item(Items.PODZOL, new AspectList().add(EARTH, 2).add(PLANT, 1).add(METAL, 1));
        item(Items.PRISMARINE, new AspectList().add(CRYSTAL, 4).add(WATER, 3).add(MANA, 1));
        item(Items.PRISMARINE_BRICKS, new AspectList().add(CRYSTAL, 5).add(WATER, 4).add(MANA, 2));
        item(Items.PUMPKIN, new AspectList().add(PLANT, 4).add(HARVEST, 2));
        item(Items.PURPUR_BLOCK, new AspectList().add(ENDER, 3).add(EARTH, 1).add(MANA, 2));
        item(Items.RABBIT_HIDE, new AspectList().add(BEAST, 1));
        item(Items.SADDLE, new AspectList().add(JOURNEY, 20).add(ARMOUR, 3).add(BEAST, 3));
        item(Items.SEA_LANTERN, new AspectList().add(CRYSTAL, 5).add(WATER, 4).add(LIGHT, 3).add(MANA, 2));
        item(Items.SLIME_BALL, new AspectList().add(SLIME, 4));
        item(Items.SNOWBALL, new AspectList().add(ICE, 1));
        item(Items.SOUL_SAND, new AspectList().add(IMPRISON, 1).add(SPIRIT, 1).add(NETHER, 1));
        item(Items.STRING, new AspectList().add(BEAST, 1).add(TOOL, 1));
        item(Items.SUGAR_CANE, new AspectList().add(WATER, 5).add(PLANT, 2));
        item(Items.TURTLE_EGG, new AspectList().add(LIFE, 6).add(BEAST, 3).add(WATER, 5));
        item(Items.WATER_BUCKET, new AspectList().add(METAL, 12).add(WATER, 5));
        item(Items.WHEAT, new AspectList().add(PLANT, 6).add(HARVEST, 2));
        item(Items.WHEAT_SEEDS, new AspectList().add(PLANT, 2).add(LIFE, 2));
        item(Items.WHITE_WOOL, new AspectList().add(FABRIC, 4).add(BEAST, 1));
        for (Aspect a: ASPECTS.values()){
            if (a == EMPTY)
                continue;
            item("arcana:" + a.id.getPath() + "_crystal", new AspectList().add(a, 1));
        }
        //for (Aspect a : AspectUtils.primalAspects){
        //    item(a.id.getPath() + "_cluster", new AspectList().add(a, 5));
        //    item(a.id.getPath() + "_cluster_seed", new AspectList().add(a, 1).add(SEEDS, 1).add(CRYSTAL, 1));
        //}
        //item(ModItems.AMBER, new AspectList().add(IMPRISON, 5));
        //item(ModItems.ARCANUM, new AspectList().add(SENSES, 5).add(MIND, 10).add(MANA, 3));
        //item(ModItems.PURIFIED_GOLD, new AspectList().add(METAL, 8).add(GREED, 4));
        //item(ModItems.PURIFIED_IRON, new AspectList().add(METAL, 8));
        //item(ModItems.PURIFIED_SILVER, new AspectList().add(METAL, 8).add(IMPRISON, 4));

        tag("arcana:shulker_boxes", new AspectList().add(VACUUM, 4).add(MANA, 2).add(ENDER, 1));
        tag("forge:cobblestone", new AspectList().add(EARTH, 3).add(CHAOS, 1));
        tag("forge:dusts/redstone", new AspectList().add(ENERGY, 3).add(MACHINE, 2));
        tag("forge:dyes", new AspectList().add(SENSES, 5));
        tag("forge:gems/diamond", new AspectList().add(CRYSTAL, 3).add(CREATION, 2));
        tag("forge:gems/emerald", new AspectList().add(CRYSTAL, 3).add(PRIDE, 2));
        tag("forge:gravel", new AspectList().add(EARTH, 2));
        tag("forge:ingots/arcanium", new AspectList().add(METAL, 3).add(MANA, 3));
        tag("forge:ingots/gold", new AspectList().add(METAL, 4).add(GREED, 2));
        tag("forge:ingots/iron", new AspectList().add(METAL, 4));
        tag("forge:ingots/silver", new AspectList().add(METAL, 4).add(IMPRISON, 2));
        tag("forge:ores/coal", new AspectList().add(EARTH, 3).add(ENERGY, 2).add(FIRE, 1));
        tag("forge:ores/diamond", new AspectList().add(EARTH, 3).add(CRYSTAL, 3).add(CREATION, 2));
        tag("forge:ores/emerald", new AspectList().add(EARTH, 3).add(CRYSTAL, 3).add(PRIDE, 2));
        tag("forge:ores/gold", new AspectList().add(EARTH, 3).add(METAL, 4).add(GREED, 2));
        tag("forge:ores/iron", new AspectList().add(EARTH, 3).add(METAL, 4));
        tag("forge:ores/redstone", new AspectList().add(EARTH, 3).add(ENERGY, 3).add(MACHINE, 2));
        tag("forge:sand", new AspectList().add(EARTH, 3).add(AIR, 2));
        tag("forge:sand/red", new AspectList().add(FIRE, 1));
        tag("forge:stone", new AspectList().add(EARTH, 4));
        tag("minecraft:banners", new AspectList().add(FABRIC, 3).add(CRAFTING, 3).add(SENSES, 2).add(PRIDE, 1));
        tag("minecraft:beds", new AspectList().add(FABRIC, 6).add(HUMAN, 4).add(CRAFTING, 2).add(SLOTH, 1));
        tag("minecraft:boats", new AspectList().add(TREE, 4).add(WATER, 2).add(JOURNEY, 1));
        tag("minecraft:buttons", new AspectList().add(MACHINE, 1));
        tag("minecraft:coals", new AspectList().add(ENERGY, 2).add(FIRE, 1));
        tag("minecraft:leaves", new AspectList().add(PLANT, 1));
        tag("minecraft:logs", new AspectList().add(TREE, 4));
        tag("minecraft:planks", new AspectList().add(TREE, 1));
        tag("minecraft:saplings", new AspectList().add(PLANT, 2).add(TREE, 1).add(LIFE, 1));
        tag("minecraft:trapdoors", new AspectList().add(MACHINE, 2).add(MOVEMENT, 1).add(IMPRISON, 1));
        tag("minecraft:wooden_trapdoors", new AspectList().add(TREE, 2));
    }

}
