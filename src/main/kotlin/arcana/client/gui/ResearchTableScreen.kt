package arcana.client.gui

import arcana.Arcana
import arcana.common.blocks.tiles.research_table.ResearchTable
import arcana.common.containers.ResearchTableContainer
import arcana.common.packets.PacketSender
import arcana.common.packets.ToggleMinigamePacket
import arcana.utils.ClientUtil
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

class ResearchTableScreen(
    container: ResearchTableContainer,
    inv: PlayerInventory,
    text: ITextComponent)
    : ContainerScreen<ResearchTableContainer>(container, inv, text) {
    var startButton: StartFinishButton? = null
    var tile: ResearchTable = container.tile

    init {
        imageWidth = WIDTH
        imageHeight = HEIGHT
    }

    override fun init() {
        super.init()
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        addButton(StartFinishButton(tile, x + 310, y + 30, 40, 15).also { startButton = it })
    }

    override fun render(ms: MatrixStack, pMouseX: Int, pMouseY: Int, pPartialTicks: Float) {
        super.render(ms, pMouseX, pMouseY, pPartialTicks)
        ms.pushPose()
        ms.translate(leftPos.toDouble(), topPos.toDouble(), 0.0)
        tile.minigame.renderPaths(ms, menu!!)
        ms.translate(20.0, 104.0, 0.0)
        tile.minigame.renderAspectCounter(ms)
        ms.popPose()
    }

    override fun tick() {
        super.tick()
        startButton!!.tick()
        tile.minigame.update()
    }

    override fun renderLabels(pMatrixStack: MatrixStack, pX: Int, pY: Int) {}
    override fun renderBg(ms: MatrixStack, pPartialTicks: Float, mouseX: Int, mouseY: Int) {
        this.renderBackground(ms)
        minecraft!!.textureManager.bind(BG)
        ClientUtil.blitOneToOne(ms, leftPos, topPos, WIDTH, HEIGHT, 0, 0, 378, 378)
    }

    class StartFinishButton(var tile: ResearchTable, pX: Int, pY: Int, pWidth: Int, pHeight: Int)
        : Button(pX, pY, pWidth, pHeight,
                 StringTextComponent(if (tile.minigame.isActive) "Finish" else "Start"),
                 { b ->
                    b.message = StringTextComponent(if (tile.minigame.isActive) "Start" else "Finish")
                    val seed = tile.level!!.random.nextLong()
                    tile.minigame.toggle(seed, null)
                    PacketSender.INSTANCE.sendToServer(ToggleMinigamePacket(seed))
                 })
    {
        init {
            tick()
        }

        fun tick() {
            active = if (!tile.minigame.isActive) {
                !tile.items.getStackInSlot(ResearchTable.INK).isEmpty && !tile.items.getStackInSlot(
                    ResearchTable.PAPER
                ).isEmpty
            } else {
                tile.minigame.canBeFinished
            }
        }
    }

    companion object {
        const val WIDTH = 378
        const val HEIGHT = 280
        private val BG = ResourceLocation(Arcana.id, "textures/gui/container/research_table_2.png")
    }
}