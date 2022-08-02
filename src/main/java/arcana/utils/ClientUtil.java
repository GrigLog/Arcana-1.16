package arcana.utils;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.util.ColorHelper.PackedColor.*;

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

    public static void renderAspectStack(MatrixStack ms, Aspect aspect, int amount, int x, int y){
        Minecraft mc = Minecraft.getInstance();
        renderAspect(ms, aspect, x, y);

        int hsb = rgbToHsb(aspect.colors.get(1));  //TODO: remove these weird calculations
        int color = hsbToRgb(color(255, (red(hsb) + 30) % 256, max(green(hsb) - 20, 0), min(blue(hsb) + 120, 255)));

        String s = String.valueOf(amount);
        MatrixStack ms2 = new MatrixStack();
        ms2.translate(0, 0, mc.getItemRenderer().blitOffset + 200.0F);
        IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        mc.font.drawInBatch(s, x + 19 - mc.font.width(s), y + 10, color, true, ms2.last().pose(), impl, false, 0, 0xf000f0);
        impl.endBatch();
    }

    public static void renderAspect(MatrixStack ms, Aspect aspect, int x, int y){
        Minecraft.getInstance().textureManager.bind(new ResourceLocation(aspect.id.getNamespace(), "textures/aspects/" + aspect.id.getPath() + ".png"));
        blitFullTexture(ms, x, y, 16, 16);
    }

    public static void blitFullTexture(MatrixStack ms, int x, int y, int w, int h){
        blit(ms.last().pose(), x, x + w, y, y + h, 0, 0, 1, 0, 1);
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



    protected static int hsbToRgb(int hsb) {
        float hue = red(hsb) / 255f, saturation = green(hsb) / 255f, brightness = blue(hsb) / 255f;
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255f + .5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6f;
            float f = h - (float)Math.floor(h);
            float p = brightness * (1 - saturation);
            float q = brightness * (1 - saturation * f);
            float t = brightness * (1 - (saturation * (1 - f)));
            switch((int)h) {
                case 0:
                    r = (int)(brightness * 255f + .5f);
                    g = (int)(t * 255 + .5);
                    b = (int)(p * 255 + .5);
                    break;
                case 1:
                    r = (int)(q * 255 + .5);
                    g = (int)(brightness * 255f + .5);
                    b = (int)(p * 255 + .5);
                    break;
                case 2:
                    r = (int)(p * 255 + .5);
                    g = (int)(brightness * 255f + .5);
                    b = (int)(t * 255 + .5);
                    break;
                case 3:
                    r = (int)(p * 255 + .5);
                    g = (int)(q * 255 + .5);
                    b = (int)(brightness * 255f + .5);
                    break;
                case 4:
                    r = (int)(t * 255 + .5);
                    g = (int)(p * 255 + .5);
                    b = (int)(brightness * 255f + .5);
                    break;
                case 5:
                    r = (int)(brightness * 255f + .5);
                    g = (int)(p * 255 + .5);
                    b = (int)(q * 255 + .5);
                    break;
            }
        }
        return color(255, r, g, b);
    }

    public static int rgbToHsb(int color) {
        int red = red(color), green = green(color), blue = blue(color);
        float hue, saturation, brightness;
        int cMax = max(red, green);
        if(blue > cMax) {
            cMax = blue;
        }
        int cMin = min(red, green);
        if(blue < cMin) {
            cMin = blue;
        }

        brightness = (float)cMax;
        if(cMax != 0) {
            saturation = (cMax - cMin) / (float) (cMax);
        } else {
            saturation = 0;
        }
        if(saturation == 0) {
            hue = 0;
        } else {
            float redC = (float)(cMax - red) / (float)(cMax - cMin);
            float greenC = (float)(cMax - green) / (float)(cMax - cMin);
            float blueC = (float)(cMax - blue) / (float)(cMax - cMin);
            if(red == cMax)
                hue = blueC - greenC;
            else if(green == cMax)
                hue = 2 + redC - blueC;
            else
                hue = 4 + greenC - redC;
            hue = hue / 6f;
            if(hue < 0)
                hue++;
        }
        return color(255, (int)(hue * 255), (int)(saturation * 255), (int)brightness);
    }
}
