package arcana.common.blocks

import arcana.utils.Util
import net.minecraft.block.Block

object ModBlocks {
    private fun <T : Block> block(name: String, block: T) = block.setRegistryName(Util.arcLoc(name)) as T

    //note: canOcclude = isSolid = you are not supposed to see through the block. noOcclusion is called for glass, for example, to avoid it becoming an X-Ray
    @JvmField val RESEARCH_TABLE_RIGHT: Block = ResearchTableRight()
    @JvmField val RESEARCH_TABLE_LEFT: Block = ResearchTableLeft()
    @JvmField val QUICK_WALL = block("quick_wall", QuickWallBlock())
}