package arcana.common.blocks.tiles.research_table

import arcana.common.blocks.ModBlocks
import arcana.common.blocks.ResearchTableLeft
import arcana.common.blocks.ResearchTableRight
import arcana.common.blocks.tiles.DefaultTile
import arcana.utils.InventoryUtils
import arcana.utils.wrappers.TileWrapper
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalBlock
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.items.ItemStackHandler

class ResearchTable @JvmOverloads constructor(type: TileEntityType<*> = this.type)
    : DefaultTile(type), ITickableTileEntity {

    var minigameItems: ItemStackHandler =
        object : ItemStackHandler(ResearchMinigame.GRID_HEIGHT * ResearchMinigame.GRID_WIDTH) {
            override fun getSlotLimit(slot: Int) = 1
            override fun onContentsChanged(slot: Int) {
                super.onContentsChanged(slot)
                setChanged()
            }
        }

    var minigame: ResearchMinigame = ResearchMinigame(this)



    var items: ItemStackHandler = object : ItemStackHandler(2) {
        override fun onContentsChanged(slot: Int) {
            val empty = getStackInSlot(slot).isEmpty
            if (slot == INK) {
                val bs = level!!.getBlockState(worldPosition)
                level!!.setBlockAndUpdate(worldPosition, bs.setValue(ResearchTableRight.INK, !empty))
            } else if (slot == PAPER) {
                var bs = level!!.getBlockState(worldPosition)
                val leftPos = worldPosition.relative(bs.getValue(HorizontalBlock.FACING).counterClockWise)
                bs = level!!.getBlockState(leftPos)
                level!!.setBlockAndUpdate(leftPos, bs.setValue(ResearchTableLeft.PAPER, !empty))
            }
            setChanged()
        }
    }

    override fun load(state: BlockState, tag: CompoundNBT) {
        super.load(state, tag)
        items.deserializeNBT(tag.getCompound("items"))
        minigameItems.deserializeNBT(tag.getCompound("minigameItems"))
        minigame.load(tag)
    }

    override fun save(tag: CompoundNBT): CompoundNBT {
        super.save(tag)
        tag.put("items", items.serializeNBT())
        tag.put("minigameItems", minigameItems.serializeNBT())
        minigame.save(tag)
        return tag
    }

    override fun setRemoved() {
        InventoryUtils.dropContents(level!!, worldPosition, items)
        InventoryUtils.dropContents(level!!, worldPosition, minigameItems)
        super.setRemoved()
    }

    override fun tick() {
        //Arcana.logger.info((level.isClientSide ? "client" : "server") + " " + minigameItems.serializeNBT().toString());
    }

    companion object {
        val type: TileEntityType<ResearchTable> =
            TileWrapper.wrap("research_table", { ResearchTable() }, ModBlocks.RESEARCH_TABLE_RIGHT)!!
        const val INK = 0
        const val PAPER = 1
    }
}