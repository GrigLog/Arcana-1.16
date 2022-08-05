package arcana.client.gui;

import arcana.Arcana;
import arcana.common.blocks.tiles.ResearchMinigame;
import arcana.common.blocks.tiles.ResearchTable;
import arcana.common.containers.ResearchTableContainer;
import arcana.common.packets.PacketSender;
import arcana.common.packets.ToggleMinigamePacket;
import arcana.utils.ClientUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static arcana.common.blocks.tiles.ResearchTable.INK;
import static arcana.common.blocks.tiles.ResearchTable.PAPER;

public class ResearchTableScreen extends ContainerScreen<ResearchTableContainer> {
    public static final int WIDTH = 378;
    public static final int HEIGHT = 280;
    private static final ResourceLocation BG = new ResourceLocation(Arcana.id, "textures/gui/container/research_table_2.png");
    StartFinishButton startButton;

    ResearchTable tile;
    public ResearchTableScreen(ResearchTableContainer container, PlayerInventory inv, ITextComponent text) {
        super(container, inv, text);
        imageWidth = WIDTH;
        imageHeight = HEIGHT;
        tile = container.tile;
    }

    @Override
    protected void init() {
        super.init();
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        addButton(startButton = new StartFinishButton(tile, x + 310, y + 30, 40, 15));
    }

    @Override
    public void tick() {
        super.tick();
        startButton.tick();
        tile.minigame.update();
    }

    @Override
    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {}

    @Override
    protected void renderBg(MatrixStack ms, float pPartialTicks, int mouseX, int mouseY) {
        this.renderBackground(ms);
        minecraft.textureManager.bind(BG);
        ClientUtil.blitOneToOne(ms, leftPos, topPos, WIDTH, HEIGHT, 0, 0, 378, 378);
    }


    protected static class StartFinishButton extends Button{
        ResearchTable tile;
        public StartFinishButton(ResearchTable tile, int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, new StringTextComponent("Start"), b -> {
                boolean active = tile.minigame.isActive();
                tile.minigame.toggle();
                PacketSender.INSTANCE.sendToServer(new ToggleMinigamePacket());
                b.setMessage(new StringTextComponent(active ? "Start" : "Finish"));
            });
            this.tile = tile;
            tick();
        }

        public void tick(){
            if (!tile.minigame.isActive()) {
                active = !tile.items.getStackInSlot(INK).isEmpty() && !tile.items.getStackInSlot(PAPER).isEmpty();
            } else {
                active = tile.minigame.canBeFinished;
            }
        }
    }
}
