package arcana.common.items;

import net.minecraft.item.Item;

public class ModItems {
    public static Item FIREWAND = new BasicWand();
    public static Item Wand = new WandItem();
    public static Item Gauntlet = new Gauntlet();
    public static FocusItem Focus = new FocusItem("focus_1");

    public static CapItem AmberCap = new CapItem("amber_cap", 50, 30, 2);
    public static CapItem BambooCap = new CapItem("bamboo_cap", 50, 30, 2);
    public static CapItem ClayCap = new CapItem("clay_cap", 32767, 32767, -1);
    public static CapItem EldritchCap = new CapItem("eldritch_cap", 70, 50, 2);
    public static CapItem HoneyCap = new CapItem("honey_cap", 50, 30, 2);
    public static CapItem CopperCap = new CapItem("copper_cap", 15, 5, 2);
    public static CapItem ElementiumCap = new CapItem("elementium_cap", 0, 0, 0);
    public static CapItem GoldCap = new CapItem("gold_cap", 25, 15, 2);
    public static CapItem IronCap = new CapItem("iron_cap", 10, 3, 1);
    public static CapItem ManasteelCap = new CapItem("manasteel_cap", 0, 0, 0);
    public static CapItem SilverCap = new CapItem("silver_cap", 0, 75, 2);
    public static CapItem TerrasteelCap = new CapItem("terrasteel_cap", 0, 0, 0);
    public static CapItem ThaumiumCap = new CapItem("thaumium_cap", 50, 25, 3);
    public static CapItem VoidCap = new CapItem("void_cap", 75, 45, 3);
    public static CapItem LeatherCap = new CapItem("leather_cap", 50, 30, 2);
    public static CapItem MechanicalCap = new CapItem("mechanical_cap", 60, 40, 2);
    public static CapItem PrismarineCap = new CapItem("prismarine_cap", 50, 30, 2);
    public static CapItem QuartzCap = new CapItem("quartz_cap", 50, 30, 2);
    public static CapItem ShulkerCap = new CapItem("shulker_cap", 0, 35, 2);
    public static CapItem NetheriteCap = new CapItem("netherite_cap", 65, 40, 3);

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
