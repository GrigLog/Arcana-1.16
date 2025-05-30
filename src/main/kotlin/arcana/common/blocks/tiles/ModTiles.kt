package arcana.common.blocks.tiles

import arcana.common.blocks.ModBlocks
import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.utils.wrappers.TileWrapper
import net.arcanamod.blocks.tiles.PedestalTileEntity
import net.minecraft.tileentity.TileEntityType

object ModTiles {
    @JvmField val RESEARCH_TABLE: TileEntityType<*> = ResearchTable.type
    @JvmField val QUICK_WALL = TileWrapper.wrap("quick_wall", ::QuickWallTile, ModBlocks.QUICK_WALL)
    @JvmField val QUANTUM_CHEST = TileWrapper.wrap("quantum_chest", ::QuantumChestTile, ModBlocks.QUANTUM_CHEST)
    @JvmField val PEDESTAL = TileWrapper.wrap("pedestal", ::PedestalTileEntity, ModBlocks.PEDESTAL)
    @JvmField val INFUSION_MATRIX = TileWrapper.wrap("infusion_matrix", ::InfusionMatrixTileEntity, ModBlocks.INFUSION_MATRIX)
}