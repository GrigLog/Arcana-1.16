package arcana.common.blocks.tiles;

import arcana.utils.InventoryUtils;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.CompoundNBT;

public class ResearchMinigame {
    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 8;

    public ResearchTable table;
    protected boolean active = false;
    public boolean canBeFinished = false;

    public ResearchMinigame(ResearchTable table){
        this.table = table;
    }

    public boolean isActive(){
        return active;
    }

    public void update(){
        canBeFinished = !table.minigameItems.getStackInSlot(0).isEmpty();
    }

    public void toggle(){
        if (active)
            finish();
        else
            start();
    }

    public void start(){
        active = true;
        table.items.extractItem(ResearchTable.PAPER, 1, false);
    }

    public void finish(){
        active = false;
        InventoryUtils.clear(table.minigameItems);
    }

    public void load(CompoundNBT tag){
        active = tag.getBoolean("active");
    }

    public CompoundNBT save(CompoundNBT tag){
        tag.putBoolean("active", active);
        return tag;
    }

}
