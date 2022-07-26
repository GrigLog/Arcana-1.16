package arcana.common.items;

import arcana.common.ArcanaGroup;
import arcana.common.blocks.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ModItems {
    public static Item FIREWAND = new BasicWand();
    public static Item RESEARCH_TABLE = new BlockItem(ModBlocks.RESEARCH_TABLE_RIGHT, ArcanaGroup.itemProps()).setRegistryName("research_table");
}
