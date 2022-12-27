package arcana.common.containers;

import arcana.client.gui.ResearchTableScreen;
import arcana.common.blocks.tiles.research_table.ResearchTable;
import arcana.common.items.ModItemTags;
import arcana.common.items.ModItems;
import arcana.common.items.aspect.AspectIcon;
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

import static arcana.common.blocks.tiles.research_table.ResearchMinigame.GRID_HEIGHT;
import static arcana.common.blocks.tiles.research_table.ResearchMinigame.GRID_WIDTH;

public class ResearchTableContainer extends Container {
    public ResearchTable tile;
    public static ContainerType<ResearchTableContainer> type = ContainerWrapper.withExtraData("research_table", ResearchTableContainer::new);

    public ResearchTableContainer(int containerId, PlayerInventory playerInv, ResearchTable table) {
        super(type, containerId);
        this.tile = table;
        addCustomSlots();
        addPlayerSlots(playerInv);
        tile.minigame.updateRoots();
    }

    public ResearchTableContainer(int containerId, PlayerInventory playerInv, PacketBuffer buf) {
        this(containerId, playerInv, (ResearchTable) playerInv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public static void open(ServerPlayerEntity serverPlayer, ResearchTable table) {
        NetworkHooks.openGui(serverPlayer,
            ContainerWrapper.namelessProvider((id, playerInv, player) -> new ResearchTableContainer(id, playerInv, table)),
            buf -> buf.writeBlockPos(table.getBlockPos()));
    }

    void addPlayerSlots(IInventory playerInventory) {
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

    private void addCustomSlots() {
        addSlot(new SlotItemHandler(tile.items, ResearchTable.INK, 300, 11) {
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return super.mayPlace(stack) && ModItemTags.SCRIBING_TOOLS.contains(stack.getItem());
            }

        });
        addSlot(new SlotItemHandler(tile.items, ResearchTable.PAPER, 343, 11) {
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return super.mayPlace(stack) && stack.getItem() == ModItems.RESEARCH_NOTE;
            }
        });
        int xBase = (int) (190 - 18 * GRID_WIDTH / 2f);
        int yBase = (int) (110 - 18 * GRID_HEIGHT / 2f);
        for (int invY = 0; invY < GRID_HEIGHT; invY++) {
            for (int invX = 0; invX < GRID_WIDTH; invX++) {
                addSlot(new SlotItemHandler(tile.minigameItems, invY * GRID_WIDTH + invX, xBase + invX * 18, yBase + invY * 18) {
                    public boolean isActive() {
                        return tile.minigame.isActive();
                    }

                    public boolean mayPickup(PlayerEntity playerIn) {
                        return !(getItem().getItem() instanceof AspectIcon);
                    }
                });
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack res = ItemStack.EMPTY; //return EMPTY if nothing was taken, slot remainders otherwise (but actually always returning the original slot contents always works wtf)
        Slot slot = slots.get(index);
        if (slot != null && slot.getItem() != ItemStack.EMPTY) {
            ItemStack selected = slot.getItem();
            res = selected.copy();
            int tableSlotsCount = 2 + GRID_WIDTH * GRID_HEIGHT;
            if (index < tableSlotsCount) { //table -> player
                if (!moveItemStackTo(selected, tableSlotsCount, slots.size(), false))
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
