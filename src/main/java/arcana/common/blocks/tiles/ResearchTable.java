package arcana.common.blocks.tiles;

import arcana.Arcana;
import arcana.common.blocks.ModBlocks;
import arcana.common.blocks.ResearchTableLeft;
import arcana.common.blocks.ResearchTableRight;
import arcana.common.containers.ResearchTableContainer;
import arcana.utils.InventoryUtils;
import arcana.utils.wrappers.TileWrapper;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ResearchTable extends DefaultTile implements ITickableTileEntity {
    public static final TileEntityType<ResearchTable> type = TileWrapper.wrap("research_table", ResearchTable::new, ModBlocks.RESEARCH_TABLE_RIGHT);
    public static final int INK = 0, PAPER = 1;

    public ItemStackHandler minigameItems = new ItemStackHandler(ResearchMinigame.GRID_HEIGHT * ResearchMinigame.GRID_WIDTH) {
        public int getSlotLimit(int slot) {
            return 1;
        }
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ResearchTable.this.setChanged();
        }
    };
    public ItemStackHandler items = new ItemStackHandler(2) {
        protected void onContentsChanged(int slot) {
            boolean empty = items.getStackInSlot(slot).isEmpty();
            if (slot == INK){
                BlockState bs = level.getBlockState(worldPosition);
                level.setBlockAndUpdate(worldPosition, bs.setValue(ResearchTableRight.INK, !empty));
            } else if (slot == PAPER){
                BlockState bs = level.getBlockState(worldPosition);
                BlockPos leftPos = worldPosition.relative(bs.getValue(HorizontalBlock.FACING).getCounterClockWise());
                bs = level.getBlockState(leftPos);
                level.setBlockAndUpdate(leftPos, bs.setValue(ResearchTableLeft.PAPER, !empty));
            }
            ResearchTable.this.setChanged();
        }
    };
    public ResearchMinigame minigame;


    public ResearchTable(TileEntityType<?> type) {
        super(type);
        minigame = new ResearchMinigame(this);
    }

    public ResearchTable(){
        this(type);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag){
        super.load(state, tag);
        items.deserializeNBT(tag.getCompound("items"));
        minigameItems.deserializeNBT(tag.getCompound("minigameItems"));
        minigame.load(tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag){
        super.save(tag);
        tag.put("items", items.serializeNBT());
        tag.put("minigameItems", minigameItems.serializeNBT());
        minigame.save(tag);
        return tag;
    }

    @Override
    public void setRemoved(){
        InventoryUtils.dropContents(level, worldPosition, items);
        InventoryUtils.dropContents(level, worldPosition, minigameItems);
        super.setRemoved();
    }

    @Override
    public void tick() {
        //Arcana.logger.info((level.isClientSide ? "client" : "server") + " " + minigameItems.serializeNBT().toString());
    }
}
