package arcana.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;

public class ClientUtil {
    public static void drawCenteredString(MatrixStack pPoseStack, FontRenderer pFont, String pText, int x, int y, int pColor) {
        pFont.drawShadow(pPoseStack, pText, (float)(x - pFont.width(pText) / 2), (float)y, pColor);
    }

    public static void drawCenteredString(MatrixStack pPoseStack, FontRenderer pFont, ITextComponent pText, int x, int y, int pColor) {
        IReorderingProcessor ireorderingprocessor = pText.getVisualOrderText();
        pFont.drawShadow(pPoseStack, ireorderingprocessor, (float)(x - pFont.width(ireorderingprocessor) / 2), (float)y, pColor);
    }

    public static void drawString(MatrixStack pPoseStack, FontRenderer pFont, String pText, int x, int y, int pColor) {
        pFont.drawShadow(pPoseStack, pText, (float)x, (float)y, pColor);
    }

    public static void drawString(MatrixStack pPoseStack, FontRenderer pFont, ITextComponent pText, int x, int y, int pColor) {
        pFont.drawShadow(pPoseStack, pText, (float)x, (float)y, pColor);
    }

    public void blitOutlineBlack(int w, int h, BiConsumer<Integer, Integer> pBoxXYConsumer) {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pBoxXYConsumer.accept(w + 1, h);
        pBoxXYConsumer.accept(w - 1, h);
        pBoxXYConsumer.accept(w, h + 1);
        pBoxXYConsumer.accept(w, h - 1);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pBoxXYConsumer.accept(w, h);
    }

    public static void blitFullTexture(MatrixStack ms, int x, int y, int w, int h){
        blit(ms.last().pose(), x, x + w, y, y + h, 0, 0, 0, 1, 1);
    }

    public static void blitOneToOne(MatrixStack ms, int x, int y, int w, int h, int texX, int texY, int atlasWidth, int atlasHeight){
        blit(ms, x, y, w, h, texX, texY, w, h, atlasWidth, atlasHeight);
    }

    //making texX and texY float might be useful to allow texX = Math.nextAfter(*, *). It can help to get rid of some rendering artifacts...
    public static void blit(MatrixStack ms, int x, int y, int w, int h, int texX, int texY, int texW, int texH, int atlasWidth, int atlasHeight) {
        blit(ms.last().pose(), x, x + w, y, y + h, 0, (float)texX / atlasWidth, ((float)texX + texW) / atlasWidth, (float)texY / atlasHeight, ((float)texY + texH) / atlasHeight);
    }

    private static void blit(Matrix4f pMatrix, int x1, int x2, int y1, int y2, int pBlitOffset, float u1, float u2, float v1, float v2) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(pMatrix, (float)x1, (float)y2, (float)pBlitOffset).uv(u1, v2).endVertex();
        bufferbuilder.vertex(pMatrix, (float)x2, (float)y2, (float)pBlitOffset).uv(u2, v2).endVertex();
        bufferbuilder.vertex(pMatrix, (float)x2, (float)y1, (float)pBlitOffset).uv(u2, v1).endVertex();
        bufferbuilder.vertex(pMatrix, (float)x1, (float)y1, (float)pBlitOffset).uv(u1, v1).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
    }
}
