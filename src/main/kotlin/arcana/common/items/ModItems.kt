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

    @JvmField val WAND = item("wand", WandItem())
    @JvmField val STAFF = item("staff", StaffItem())
    @JvmField val GAUNTLET = item("gauntlet", Gauntlet())

    @JvmField val FOCUS = item("focus", FocusItem())
    //@JvmField val AMBER_CAP = CapItem(arcLoc("amber"), 50, 30, 2)
    //@JvmField val BAMBOO_CAP = CapItem(arcLoc("bamboo"), 50, 30, 2)
    //@JvmField val CLAY_CAP = CapItem(arcLoc("clay"), 32767, 32767, -1)
    // @JvmField val ELDRITCH_CAP = CapItem(arcLoc("eldritch"), 70, 50, 2)
    //@JvmField val HONEY_CAP = CapItem(arcLoc("honey"), 50, 30, 2)
    //@JvmField val COPPER_CAP = CapItem(arcLoc("copper"), 15, 5, 2)
    //@JvmField val ELEMENTIUM_CAP = CapItem(arcLoc("elementium"), 0, 0, 0)
    @JvmField val GOLD_CAP = CapItem(arcLoc("gold"), 25, 15, 2)
    @JvmField val IRON_CAP = CapItem(arcLoc("iron"), 10, 3, 1)
    //@JvmField val MANASTEEL_CAP = CapItem(arcLoc("manasteel"), 0, 0, 0)
    @JvmField val SILVER_CAP = CapItem(arcLoc("silver"), 0, 75, 2)
    //@JvmField val TERRASTEEL_CAP = CapItem(arcLoc("terrasteel"), 0, 0, 0)
    @JvmField val THAUMIUM_CAP = CapItem(arcLoc("thaumium"), 50, 25, 3)
    @JvmField val VOID_CAP = CapItem(arcLoc("void"), 75, 45, 3)
    ///@JvmField val LEATHER_CAP = CapItem(arcLoc("leather"), 50, 30, 2)
    //@JvmField val MECHANICAL_CAP = CapItem(arcLoc("mechanical"), 60, 40, 2)
    //@JvmField val PRISMARINE_CAP = CapItem(arcLoc("prismarine"), 50, 30, 2)
    //@JvmField val QUARTZ_CAP = CapItem(arcLoc("quartz"), 50, 30, 2)
    //@JvmField val SHULKER_CAP = CapItem(arcLoc("shulker"), 0, 35, 2)
    //@JvmField val NETHERITE_CAP = CapItem(arcLoc("netherite"), 65, 40, 3)

    @JvmField val WOOD_CORE = CoreItem(arcLoc("wood"), 5, 1, 1)
    @JvmField val GREATWOOD_CORE = CoreItem(arcLoc("greatwood"), 25, 3, 2)
    //@JvmField val TAINTED_CORE = CoreItem(arcLoc("tainted"), 200, 30, 2)
    @JvmField val DAIR_CORE = CoreItem(arcLoc("dair"), 10, 1, 3)
    //@JvmField val HAWTHORN_CORE = CoreItem(arcLoc("hawthorn"), 15, 1, 3)
    @JvmField val SILVERWOOD_CORE = CoreItem(arcLoc("silverwood"), 100, 5, 4)
    @JvmField val ARCANIUM_CORE = CoreItem(arcLoc("arcanium"), 150, 10, 4)
    @JvmField val BLAZE_CORE = CoreItem(arcLoc("blaze"), 199, 10, 2)
    //@JvmField val ENDROD_CORE = CoreItem(arcLoc("endrod"), 225, 15, 4)
    //@JvmField val BONE_CORE = CoreItem(arcLoc("bone"), 100, 5, 2)
    @JvmField val ICE_CORE = CoreItem(arcLoc("ice"), 100, 10, 2)
    //@JvmField val ARCANE_STONE_CORE = CoreItem(arcLoc("arcane_stone"), 100, 10, 2)
    //@JvmField val OBSIDIAN_CORE = CoreItem(arcLoc("obsidian"), 100, 5, 2)
    //@JvmField val SUGAR_CANE_CORE = CoreItem(arcLoc("sugar_cane"), 100, 10, 2)
    //@JvmField val MECHANICAL_CORE = CoreItem(arcLoc("mechanical"), 125, 15, 2)
    //@JvmField val ELDRITCH_CORE = CoreItem(arcLoc("eldritch"), 250, 30, 2)
    //@JvmField val CLAY_CORE = CoreItem(arcLoc("clay"), Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt(), -1)
}