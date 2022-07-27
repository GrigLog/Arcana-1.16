package arcana.common.blocks.tiles;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class ResearchTable extends TileEntity {
    public static final TileEntityType<ResearchTable> type = TileWrapper.wrap("research_table", ResearchTable::new, ModBlocks.RESEARCH_TABLE_RIGHT);
    public ResearchTable(TileEntityType<?> type) {
        super(type);
    }
    public ResearchTable(){
        super(type);
    }
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
    public static final int INK = 0, PAPER = 1;

    public ResearchTableContainer getContainer(int id, PlayerInventory inv, PlayerEntity player){
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer(8, 8));
        buf.writeBlockPos(worldPosition);
        return new ResearchTableContainer(id, inv, buf);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound){
        super.load(state, compound);
        items.deserializeNBT(compound.getCompound("items"));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound){
        super.save(compound);
        compound.put("items", items.serializeNBT());
        return compound;
    }

    @Override
    public void setRemoved(){
        InventoryUtils.dropContents(level, worldPosition, items);
        super.setRemoved();
    }
}
