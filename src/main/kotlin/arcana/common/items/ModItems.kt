package arcana.common.items

import arcana.common.blocks.ModBlocks
import arcana.common.items.wand.*
import arcana.utils.Util.arcLoc
import net.minecraft.item.BlockItem
import net.minecraft.item.Item


object ModItems {
    private fun <T : Item> item(name: String, item: T) = item.setRegistryName(arcLoc(name)) as T

    @JvmField val ARCANUM = item("arcanum", ArcanumItem())
    @JvmField val FIREWAND = item("basicwand", BasicWand())
    @JvmField val RESEARCH_TABLE = item("research_table", BlockItem(ModBlocks.RESEARCH_TABLE_RIGHT, ArcanaGroup.props))
    @JvmField val SCRIBING_TOOLS = item("scribing_tools", Item(ArcanaGroup.props.durability(100).setNoRepair()))
    @JvmField val RESEARCH_NOTE = item("research_note", ResearchNote(ArcanaGroup.props, false))
    @JvmField val RESEARCH_NOTE_COMPLETE = item("research_note_complete",
                                          ResearchNote(ArcanaGroup.props, true))

    @JvmField val WAND = item("wand", WandItem(ArcanaGroup.props))
    @JvmField val STAFF = item("staff", StaffItem())
    @JvmField val GAUNTLET = item("gauntlet", Gauntlet())

    @JvmField val FOCUS = item("focus", FocusItem())
    @JvmField val AMBER_CAP = CapItem(arcLoc("amber_cap"), 50, 30, 2)
    @JvmField val BAMBOO_CAP = CapItem(arcLoc("bamboo_cap"), 50, 30, 2)
    @JvmField val CLAY_CAP = CapItem(arcLoc("clay_cap"), 32767, 32767, -1)
    @JvmField val ELDRITCH_CAP = CapItem(arcLoc("eldritch_cap"), 70, 50, 2)
    @JvmField val HONEY_CAP = CapItem(arcLoc("honey_cap"), 50, 30, 2)
    @JvmField val COPPER_CAP = CapItem(arcLoc("copper_cap"), 15, 5, 2)
    @JvmField val ELEMENTIUM_CAP = CapItem(arcLoc("elementium_cap"), 0, 0, 0)
    @JvmField val GOLD_CAP = CapItem(arcLoc("gold_cap"), 25, 15, 2)
    @JvmField val IRON_CAP = CapItem(arcLoc("iron_cap"), 10, 3, 1)
    @JvmField val MANASTEEL_CAP = CapItem(arcLoc("manasteel_cap"), 0, 0, 0)
    @JvmField val SILVER_CAP = CapItem(arcLoc("silver_cap"), 0, 75, 2)
    @JvmField val TERRASTEEL_CAP = CapItem(arcLoc("terrasteel_cap"), 0, 0, 0)
    @JvmField val THAUMIUM_CAP = CapItem(arcLoc("thaumium_cap"), 50, 25, 3)
    @JvmField val VOID_CAP = CapItem(arcLoc("void_cap"), 75, 45, 3)
    @JvmField val LEATHER_CAP = CapItem(arcLoc("leather_cap"), 50, 30, 2)
    @JvmField val MECHANICAL_CAP = CapItem(arcLoc("mechanical_cap"), 60, 40, 2)
    @JvmField val PRISMARINE_CAP = CapItem(arcLoc("prismarine_cap"), 50, 30, 2)
    @JvmField val QUARTZ_CAP = CapItem(arcLoc("quartz_cap"), 50, 30, 2)
    @JvmField val SHULKER_CAP = CapItem(arcLoc("shulker_cap"), 0, 35, 2)
    @JvmField val NETHERITE_CAP = CapItem(arcLoc("netherite_cap"), 65, 40, 3)

    @JvmField val WOOD_WAND_CORE = CoreItem(arcLoc("wood_wand_core"), 5, 1, 1)
    @JvmField val GREATWOOD_WAND_CORE = CoreItem(arcLoc("greatwood_wand_core"), 25, 3, 2)
    @JvmField val TAINTED_WAND_CORE = CoreItem(arcLoc("tainted_wand_core"), 200, 30, 2)
    @JvmField val DAIR_WAND_CORE = CoreItem(arcLoc("dair_wand_core"), 10, 1, 3)
    @JvmField val HAWTHORN_WAND_CORE = CoreItem(arcLoc("hawthorn_wand_core"), 15, 1, 3)
    @JvmField val SILVERWOOD_WAND_CORE = CoreItem(arcLoc("silverwood_wand_core"), 100, 5, 4)
    @JvmField val ARCANIUM_WAND_CORE = CoreItem(arcLoc("arcanium_wand_core"), 150, 10, 4)
    @JvmField val BLAZE_WAND_CORE = CoreItem(arcLoc("blaze_wand_core"), 199, 10, 2)
    @JvmField val ENDROD_WAND_CORE = CoreItem(arcLoc("endrod_wand_core"), 225, 15, 4)
    @JvmField val BONE_WAND_CORE = CoreItem(arcLoc("bone_wand_core"), 100, 5, 2)
    @JvmField val ICE_WAND_CORE = CoreItem(arcLoc("ice_wand_core"), 100, 10, 2)
    @JvmField val ARCANE_STONE_WAND_CORE = CoreItem(arcLoc("arcane_stone_wand_core"), 100, 10, 2)
    @JvmField val OBSIDIAN_WAND_CORE = CoreItem(arcLoc("obsidian_wand_core"), 100, 5, 2)
    @JvmField val SUGAR_CANE_WAND_CORE = CoreItem(arcLoc("sugar_cane_wand_core"), 100, 10, 2)
    @JvmField val MECHANICAL_WAND_CORE = CoreItem(arcLoc("mechanical_wand_core"), 125, 15, 2)
    @JvmField val ELDRITCH_WAND_CORE = CoreItem(arcLoc("eldritch_wand_core"), 250, 30, 2)
    @JvmField val CLAY_WAND_CORE = CoreItem(arcLoc("clay_wand_core"), Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt(), -1)
}