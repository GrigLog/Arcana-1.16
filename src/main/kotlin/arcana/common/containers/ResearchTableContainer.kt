package arcana.common.containers

import arcana.client.gui.ResearchTableScreen
import arcana.common.blocks.tiles.research_table.ResearchMinigame
import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.common.items.ModItemTags
import arcana.common.items.ModItems
import arcana.common.items.aspect.AspectIcon
import arcana.utils.wrappers.ContainerWrapper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkHooks
import net.minecraftforge.items.SlotItemHandler
import javax.annotation.Nonnull

class ResearchTableContainer(
    containerId: Int,
    playerInv: PlayerInventory,
    var tile: ResearchTable)
    : Container(type, containerId) {
    init {
        addCustomSlots()
        addPlayerSlots(playerInv)
        tile.minigame.updateRoots()
    }

    constructor(containerId: Int, playerInv: PlayerInventory, buf: PacketBuffer)
        : this(containerId, playerInv,
           playerInv.player.level.getBlockEntity(buf.readBlockPos()) as ResearchTable
    )

    fun addPlayerSlots(playerInventory: IInventory) {
        val hotX = 79
        val invX = 139
        val baseY: Int = ResearchTableScreen.HEIGHT - 61
        for (row in 0..2) {
            for (col in 0..8) {
                val x = invX + col * 18
                val y = row * 18 + baseY
                addSlot(Slot(playerInventory, col + row * 9 + 9, x, y))
            }
        }
        for (row in 0..2) {
            for (col in 0..2) {
                val x = hotX + col * 18
                val y = row * 18 + baseY
                addSlot(Slot(playerInventory, col + row * 3, x, y))
            }
        }
    }

    private fun addCustomSlots() {
        addSlot(object : SlotItemHandler(tile.items, ResearchTable.INK, 300, 11) {
            override fun mayPlace(@Nonnull stack: ItemStack): Boolean {
                return super.mayPlace(stack) && ModItemTags.SCRIBING_TOOLS.contains(stack.item)
            }
        })
        addSlot(object : SlotItemHandler(tile.items, ResearchTable.PAPER, 343, 11) {
            override fun mayPlace(@Nonnull stack: ItemStack): Boolean {
                return super.mayPlace(stack) && stack.item === ModItems.RESEARCH_NOTE
            }
        })
        val xBase: Int = (190 - 18 * ResearchMinigame.GRID_WIDTH / 2f).toInt()
        val yBase: Int = (110 - 18 * ResearchMinigame.GRID_HEIGHT / 2f).toInt()
        for (invY in 0 until ResearchMinigame.GRID_HEIGHT) {
            for (invX in 0 until ResearchMinigame.GRID_WIDTH) {
                addSlot(object : SlotItemHandler(
                    tile.minigameItems,
                    invY * ResearchMinigame.GRID_WIDTH + invX,
                    xBase + invX * 18,
                    yBase + invY * 18
                ) {
                    override fun isActive(): Boolean {
                        return tile.minigame.isActive
                    }

                    override fun mayPickup(playerIn: PlayerEntity): Boolean {
                        return item.item !is AspectIcon
                    }
                })
            }
        }
    }

    override fun quickMoveStack(player: PlayerEntity, index: Int): ItemStack {
        var res =
            ItemStack.EMPTY //return EMPTY if nothing was taken, slot remainders otherwise (but actually always returning the original slot contents always works wtf)
        val slot = slots[index]
        if (slot != null && slot.item != ItemStack.EMPTY) {
            val selected = slot.item
            res = selected.copy()
            val tableSlotsCount: Int =
                2 + ResearchMinigame.GRID_WIDTH * ResearchMinigame.GRID_HEIGHT
            if (index < tableSlotsCount) { //table -> player
                if (!moveItemStackTo(selected, tableSlotsCount, slots.size, false)) return ItemStack.EMPTY
            } else { //player -> table
                if (!moveItemStackTo(selected, 0, 2, false)) return ItemStack.EMPTY
            }
            slot.set(selected)
        }
        return res
    }

    override fun stillValid(player: PlayerEntity): Boolean {
        return player.blockPosition().distManhattan(tile.blockPos) <= 5
    }

    companion object {
        var type: ContainerType<ResearchTableContainer> = ContainerWrapper.withExtraData("research_table", ::ResearchTableContainer)

        fun open(serverPlayer: ServerPlayerEntity, table: ResearchTable) {
            NetworkHooks.openGui(serverPlayer,
                                 ContainerWrapper.namelessProvider{ id, playerInv, player -> ResearchTableContainer(id, playerInv, table)},
                                 { buf: PacketBuffer -> buf.writeBlockPos(table.blockPos) })
        }
    }
}