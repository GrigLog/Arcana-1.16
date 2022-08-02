package arcana.common.containers;

import arcana.Arcana;
import arcana.client.gui.ResearchTableScreen;
import arcana.common.blocks.tiles.ResearchTable;
import arcana.common.items.ModItemTags;
import arcana.common.items.ModItems;
import arcana.utils.wrappers.ContainerWrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ResearchTableContainer extends Container {
    public ResearchTable tile;
    public static ContainerType<ResearchTableContainer> type = ContainerWrapper.withExtraData("research_table", ResearchTableContainer::new);
    public ResearchTableContainer(int containerId, PlayerInventory playerInv, ResearchTable table){
        super(type, containerId);
        this.tile = table;
        addCustomSlots();
        addPlayerSlots(playerInv);
    }

    public ResearchTableContainer(int containerId, PlayerInventory playerInv, PacketBuffer buf){
        this(containerId, playerInv, (ResearchTable) playerInv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public static void open(ServerPlayerEntity serverPlayer, ResearchTable table){
        NetworkHooks.openGui(serverPlayer,
            ContainerWrapper.namelessProvider((id, playerInv, player) -> new ResearchTableContainer(id, playerInv, table)),
            buf -> buf.writeBlockPos(table.getBlockPos()));
    }

    void addPlayerSlots(IInventory playerInventory){
        int hotX = 79, invX = 139, baseY = ResearchTableScreen.HEIGHT - 61;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = invX + col * 18;
                int y = row * 18 + baseY;
                addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int x = hotX + col * 18;
                int y = row * 18 + baseY;
                addSlot(new Slot(playerInventory, col + row * 3, x, y));
            }
        }
    }

    private void addCustomSlots(){
        addSlot(new SlotItemHandler(tile.items, ResearchTable.INK, 137, 11){
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return super.mayPlace(stack) && ModItemTags.SCRIBING_TOOLS.contains(stack.getItem());
            }

        });
        addSlot(new SlotItemHandler(tile.items, ResearchTable.PAPER, 155, 11){
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return super.mayPlace(stack) && stack.getItem() == ModItems.RESEARCH_NOTE;
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack res = ItemStack.EMPTY; //return EMPTY if nothing was taken, slot remainders otherwise (but actually always returning the original slot contents always works wtf)
        Slot slot = slots.get(index);

        if (slot != null && slot.getItem() != ItemStack.EMPTY) {
            ItemStack selected = slot.getItem();
            res = selected.copy();
            if (index < 2) { //table -> player
                if (!moveItemStackTo(selected, 2, slots.size(), false))
                    return ItemStack.EMPTY;
            } else { //player -> table
                if (!moveItemStackTo(selected, 0, 2, false))
                    return ItemStack.EMPTY;
            }
            slot.set(selected);
        }
        return res;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.blockPosition().distManhattan(tile.getBlockPos()) <= 5;
    }
}
