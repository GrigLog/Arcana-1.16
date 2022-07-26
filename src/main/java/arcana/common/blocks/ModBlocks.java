package arcana.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
    //note: canOcclude = isSolid = you are not supposed to see through the block. noOcclusion is called for glass, for example, to avoid it becoming an X-Ray
    public static Block RESEARCH_TABLE_RIGHT = new ResearchTableRight();
    public static Block RESEARCH_TABLE_LEFT = new ResearchTableLeft();
}
