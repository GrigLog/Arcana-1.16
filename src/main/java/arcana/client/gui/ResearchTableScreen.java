package arcana.client.gui;

import arcana.Arcana;
import arcana.common.blocks.tiles.ResearchTable;
import arcana.common.containers.ResearchTableContainer;
import arcana.utils.ClientUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ResearchTableScreen extends ContainerScreen<ResearchTableContainer> {
    public static final int WIDTH = 378;
    public static final int HEIGHT = 280;
    private static final ResourceLocation BG = new ResourceLocation(Arcana.id, "textures/gui/container/research_table.png");

    ResearchTable tile;
    public ResearchTableScreen(ResearchTableContainer container, PlayerInventory inv, ITextComponent text) {
        super(container, inv, text);
        imageWidth = WIDTH;
        imageHeight = HEIGHT;
        tile = container.tile;
    }

    @Override
    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {}

    @Override
    protected void renderBg(MatrixStack ms, float pPartialTicks, int mouseX, int mouseY) {
        this.renderBackground(ms);
        minecraft.textureManager.bind(BG);
        ClientUtil.blitOneToOne(ms, leftPos, topPos, WIDTH, HEIGHT, 0, 0, 378, 378);
    }
}
