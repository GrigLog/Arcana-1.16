package arcana.common.blocks

import net.minecraft.block.Block

object ModBlocks {
    //note: canOcclude = isSolid = you are not supposed to see through the block. noOcclusion is called for glass, for example, to avoid it becoming an X-Ray
    @JvmField val RESEARCH_TABLE_RIGHT: Block = ResearchTableRight()
    @JvmField val RESEARCH_TABLE_LEFT: Block = ResearchTableLeft()
}