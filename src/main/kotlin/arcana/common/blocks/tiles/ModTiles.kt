package arcana.common.blocks.tiles

import arcana.common.blocks.tiles.research_table.ResearchTable
import net.minecraft.tileentity.TileEntityType

object ModTiles {
    @JvmField val RESEARCH_TABLE: TileEntityType<*> = ResearchTable.type
}