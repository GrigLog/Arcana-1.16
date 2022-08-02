package arcana.common.items;

import arcana.common.ArcanaGroup;
import arcana.common.blocks.ModBlocks;
import arcana.common.items.spell.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ModItems {
    public static Item Arcanum = new ArcanumItem();
    public static Item FIREWAND = new BasicWand();
    public static Item RESEARCH_TABLE = new BlockItem(ModBlocks.RESEARCH_TABLE_RIGHT, ArcanaGroup.itemProps()).setRegistryName("research_table");
    public static Item SCRIBING_TOOLS = new Item(ArcanaGroup.itemProps().durability(100).setNoRepair()).setRegistryName("scribing_tools");
    public static Item RESEARCH_NOTE = new ResearchNote(ArcanaGroup.itemProps(), false).setRegistryName("research_note");
    public static Item RESEARCH_NOTE_COMPLETE = new ResearchNote(ArcanaGroup.itemProps(), true).setRegistryName("research_note_complete");

    public static Item WAND = new WandItem();
    public static Item STAFF = new StaffItem();
    public static Item GAUNTLET = new Gauntlet();
    public static arcana.common.items.FocusItem FOCUS = new arcana.common.items.FocusItem("focus_1");

    public static CapItem AMBER_CAP = new CapItem("amber_cap", 50, 30, 2);
    public static CapItem BAMBOO_CAP = new CapItem("bamboo_cap", 50, 30, 2);
    public static CapItem CLAY_CAP = new CapItem("clay_cap", 32767, 32767, -1);
    public static CapItem ELDRITCH_CAP = new CapItem("eldritch_cap", 70, 50, 2);
    public static CapItem HONEY_CAP = new CapItem("honey_cap", 50, 30, 2);
    public static CapItem COPPER_CAP = new CapItem("copper_cap", 15, 5, 2);
    public static CapItem ELEMENTIUM_CAP = new CapItem("elementium_cap", 0, 0, 0);
    public static CapItem GOLD_CAP = new CapItem("gold_cap", 25, 15, 2);
    public static CapItem IRON_CAP = new CapItem("iron_cap", 10, 3, 1);
    public static CapItem MANASTEEL_CAP = new CapItem("manasteel_cap", 0, 0, 0);
    public static CapItem SILVER_CAP = new CapItem("silver_cap", 0, 75, 2);
    public static CapItem TERRASTEEL_CAP = new CapItem("terrasteel_cap", 0, 0, 0);
    public static CapItem THAUMIUM_CAP = new CapItem("thaumium_cap", 50, 25, 3);
    public static CapItem VOID_CAP = new CapItem("void_cap", 75, 45, 3);
    public static CapItem LEATHER_CAP = new CapItem("leather_cap", 50, 30, 2);
    public static CapItem MECHANICAL_CAP = new CapItem("mechanical_cap", 60, 40, 2);
    public static CapItem PRISMARINE_CAP = new CapItem("prismarine_cap", 50, 30, 2);
    public static CapItem QUARTZ_CAP = new CapItem("quartz_cap", 50, 30, 2);
    public static CapItem SHULKER_CAP = new CapItem("shulker_cap", 0, 35, 2);
    public static CapItem NETHERITE_CAP = new CapItem("netherite_cap", 65, 40, 3);

    public static CoreItem WOOD_WAND_CORE = new CoreItem("wood_wand_core", 5, 1, 1);
    public static CoreItem GREATWOOD_WAND_CORE = new CoreItem("greatwood_wand_core", 25, 3, 2);
    public static CoreItem TAINTED_WAND_CORE = new CoreItem("tainted_wand_core", 200, 30, 2);
    public static CoreItem DAIR_WAND_CORE = new CoreItem("dair_wand_core", 10, 1, 3);
    public static CoreItem HAWTHORN_WAND_CORE = new CoreItem("hawthorn_wand_core", 15, 1, 3);
    public static CoreItem SILVERWOOD_WAND_CORE = new CoreItem("silverwood_wand_core", 100, 5, 4);
    public static CoreItem ARCANIUM_WAND_CORE = new CoreItem("arcanium_wand_core", 150, 10, 4);
    public static CoreItem BLAZE_WAND_CORE = new CoreItem("blaze_wand_core", 199, 10, 2);
    public static CoreItem ENDROD_WAND_CORE = new CoreItem("endrod_wand_core", 225, 15, 4);
    public static CoreItem BONE_WAND_CORE = new CoreItem("bone_wand_core", 100, 5, 2);
    public static CoreItem ICE_WAND_CORE = new CoreItem("ice_wand_core", 100, 10, 2);
    public static CoreItem ARCANE_STONE_WAND_CORE = new CoreItem("arcane_stone_wand_core", 100, 10, 2);
    public static CoreItem OBSIDIAN_WAND_CORE = new CoreItem("obsidian_wand_core", 100, 5, 2);
    public static CoreItem SUGAR_CANE_WAND_CORE = new CoreItem("sugar_cane_wand_core", 100, 10, 2);
    public static CoreItem MECHANICAL_WAND_CORE = new CoreItem("mechanical_wand_core", 125, 15, 2);
    public static CoreItem ELDRITCH_WAND_CORE = new CoreItem("eldritch_wand_core", 250, 30, 2);
    public static CoreItem CLAY_WAND_CORE = new CoreItem("clay_wand_core", Short.MAX_VALUE, Short.MAX_VALUE, -1);
}
