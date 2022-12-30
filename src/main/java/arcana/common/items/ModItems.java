package arcana.common.items;

import arcana.common.blocks.ModBlocks;
import arcana.common.items.wand.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import static arcana.utils.Util.arcLoc;

public class ModItems {
    public static Item Arcanum = new ArcanumItem();
    public static Item FIREWAND = new BasicWand();
    public static Item RESEARCH_TABLE = new BlockItem(ModBlocks.RESEARCH_TABLE_RIGHT, ArcanaGroup.itemProps()).setRegistryName("research_table");
    public static Item SCRIBING_TOOLS = new Item(ArcanaGroup.itemProps().durability(100).setNoRepair()).setRegistryName("scribing_tools");
    public static Item RESEARCH_NOTE = new ResearchNote(ArcanaGroup.itemProps(), false).setRegistryName("research_note");
    public static Item RESEARCH_NOTE_COMPLETE = new ResearchNote(ArcanaGroup.itemProps(), true).setRegistryName("research_note_complete");

    public static WandItem WAND = new WandItem();
    //public static Item STAFF = new StaffItem();
    public static Item GAUNTLET = new Gauntlet();
    public static FocusItem FOCUS = new FocusItem(arcLoc("focus_1"));

    public static CapItem AMBER_CAP = new CapItem(arcLoc("amber_cap"), 50, 30, 2);
    public static CapItem BAMBOO_CAP = new CapItem(arcLoc("bamboo_cap"), 50, 30, 2);
    public static CapItem CLAY_CAP = new CapItem(arcLoc("clay_cap"), 32767, 32767, -1);
    public static CapItem ELDRITCH_CAP = new CapItem(arcLoc("eldritch_cap"), 70, 50, 2);
    public static CapItem HONEY_CAP = new CapItem(arcLoc("honey_cap"), 50, 30, 2);
    public static CapItem COPPER_CAP = new CapItem(arcLoc("copper_cap"), 15, 5, 2);
    public static CapItem ELEMENTIUM_CAP = new CapItem(arcLoc("elementium_cap"), 0, 0, 0);
    public static CapItem GOLD_CAP = new CapItem(arcLoc("gold_cap"), 25, 15, 2);
    public static CapItem IRON_CAP = new CapItem(arcLoc("iron_cap"), 10, 3, 1);
    public static CapItem MANASTEEL_CAP = new CapItem(arcLoc("manasteel_cap"), 0, 0, 0);
    public static CapItem SILVER_CAP = new CapItem(arcLoc("silver_cap"), 0, 75, 2);
    public static CapItem TERRASTEEL_CAP = new CapItem(arcLoc("terrasteel_cap"), 0, 0, 0);
    public static CapItem THAUMIUM_CAP = new CapItem(arcLoc("thaumium_cap"), 50, 25, 3);
    public static CapItem VOID_CAP = new CapItem(arcLoc("void_cap"), 75, 45, 3);
    public static CapItem LEATHER_CAP = new CapItem(arcLoc("leather_cap"), 50, 30, 2);
    public static CapItem MECHANICAL_CAP = new CapItem(arcLoc("mechanical_cap"), 60, 40, 2);
    public static CapItem PRISMARINE_CAP = new CapItem(arcLoc("prismarine_cap"), 50, 30, 2);
    public static CapItem QUARTZ_CAP = new CapItem(arcLoc("quartz_cap"), 50, 30, 2);
    public static CapItem SHULKER_CAP = new CapItem(arcLoc("shulker_cap"), 0, 35, 2);
    public static CapItem NETHERITE_CAP = new CapItem(arcLoc("netherite_cap"), 65, 40, 3);

    public static CoreItem WOOD_WAND_CORE = new CoreItem(arcLoc("wood_wand_core"), 5, 1, 1);
    public static CoreItem GREATWOOD_WAND_CORE = new CoreItem(arcLoc("greatwood_wand_core"), 25, 3, 2);
    public static CoreItem TAINTED_WAND_CORE = new CoreItem(arcLoc("tainted_wand_core"), 200, 30, 2);
    public static CoreItem DAIR_WAND_CORE = new CoreItem(arcLoc("dair_wand_core"), 10, 1, 3);
    public static CoreItem HAWTHORN_WAND_CORE = new CoreItem(arcLoc("hawthorn_wand_core"), 15, 1, 3);
    public static CoreItem SILVERWOOD_WAND_CORE = new CoreItem(arcLoc("silverwood_wand_core"), 100, 5, 4);
    public static CoreItem ARCANIUM_WAND_CORE = new CoreItem(arcLoc("arcanium_wand_core"), 150, 10, 4);
    public static CoreItem BLAZE_WAND_CORE = new CoreItem(arcLoc("blaze_wand_core"), 199, 10, 2);
    public static CoreItem ENDROD_WAND_CORE = new CoreItem(arcLoc("endrod_wand_core"), 225, 15, 4);
    public static CoreItem BONE_WAND_CORE = new CoreItem(arcLoc("bone_wand_core"), 100, 5, 2);
    public static CoreItem ICE_WAND_CORE = new CoreItem(arcLoc("ice_wand_core"), 100, 10, 2);
    public static CoreItem ARCANE_STONE_WAND_CORE = new CoreItem(arcLoc("arcane_stone_wand_core"), 100, 10, 2);
    public static CoreItem OBSIDIAN_WAND_CORE = new CoreItem(arcLoc("obsidian_wand_core"), 100, 5, 2);
    public static CoreItem SUGAR_CANE_WAND_CORE = new CoreItem(arcLoc("sugar_cane_wand_core"), 100, 10, 2);
    public static CoreItem MECHANICAL_WAND_CORE = new CoreItem(arcLoc("mechanical_wand_core"), 125, 15, 2);
    public static CoreItem ELDRITCH_WAND_CORE = new CoreItem(arcLoc("eldritch_wand_core"), 250, 30, 2);
    public static CoreItem CLAY_WAND_CORE = new CoreItem(arcLoc("clay_wand_core"), Short.MAX_VALUE, Short.MAX_VALUE, -1);
}
