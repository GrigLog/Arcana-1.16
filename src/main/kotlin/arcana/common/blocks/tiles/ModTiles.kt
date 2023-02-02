package arcana.common.blocks.tiles

import arcana.common.blocks.ModBlocks
import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.utils.wrappers.TileWrapper
import net.minecraft.tileentity.TileEntityType

object ModTiles {
    @JvmField val RESEARCH_TABLE: TileEntityType<*> = ResearchTable.type
    @JvmField val QUICK_WALL = TileWrapper.wrap("quick_wall", ::QuickWallTile, ModBlocks.QUICK_WALL)
}