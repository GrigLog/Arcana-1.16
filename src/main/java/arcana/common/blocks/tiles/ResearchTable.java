package arcana.common.blocks.tiles;

import arcana.common.blocks.ModBlocks;
import arcana.common.containers.ResearchTableContainer;
import arcana.utils.wrappers.TileWrapper;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

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
}
