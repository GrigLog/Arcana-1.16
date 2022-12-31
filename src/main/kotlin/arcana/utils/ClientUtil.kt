package arcana.utils

import arcana.common.aspects.Aspect
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ColorHelper.PackedColor.*
import net.minecraft.util.IReorderingProcessor
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.text.ITextComponent
import java.util.function.BiConsumer
import kotlin.math.max
import kotlin.math.min

object ClientUtil {
    //the following 4 functions add casts Int->Float. They are not much needed.
    fun drawCenteredString(ms: MatrixStack, font: FontRenderer, text: String, x: Int, y: Int, col: Int) =
        font.drawShadow(ms, text, (x - font.width(text) / 2).toFloat(), y.toFloat(), col)

    fun drawCenteredString(ms: MatrixStack, font: FontRenderer, text: ITextComponent, x: Int, y: Int, col: Int) {
        val ireorderingprocessor: IReorderingProcessor = text.visualOrderText
        font.drawShadow(ms, ireorderingprocessor, (x - font.width(ireorderingprocessor) / 2).toFloat(), y.toFloat(), col)
    }
    fun drawString(ms: MatrixStack, font: FontRenderer, text: String, x: Int, y: Int, col: Int) =
        font.drawShadow(ms, text, x.toFloat(), y.toFloat(), col)

    fun drawString(ms: MatrixStack, font: FontRenderer, text: ITextComponent, x: Int, y: Int, col: Int) =
        font.drawShadow(ms, text, x.toFloat(), y.toFloat(), col)

    fun blitOutlineBlack(w: Int, h: Int, pBoxXYConsumer: BiConsumer<Int, Int>) {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        pBoxXYConsumer.accept(w + 1, h)
        pBoxXYConsumer.accept(w - 1, h)
        pBoxXYConsumer.accept(w, h + 1)
        pBoxXYConsumer.accept(w, h - 1)
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        pBoxXYConsumer.accept(w, h)
    }

    fun renderAspectStack(ms: MatrixStack, aspect: Aspect, amount: Int, x: Int, y: Int) {
        val mc = Minecraft.getInstance()
        renderAspect(ms, aspect, x, y)
        val hsb = rgbToHsb(aspect.colors[1]) //TODO: remove these weird calculations
        val color = hsbToRgb(color(255, (red(hsb) + 30) % 256, max(green(hsb) - 20, 0), min(blue(hsb) + 120, 255)))
        val s = amount.toString()
        val ms2 = MatrixStack()
        ms2.translate(0.0, 0.0, (mc.itemRenderer.blitOffset + 200.0f).toDouble())
        val impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().builder)
        mc.font.drawInBatch(s, (x + 19 - mc.font.width(s)).toFloat(), (y + 10).toFloat(), color, true, ms2.last().pose(), impl, false, 0, 0xf000f0)
        impl.endBatch()
    }

    fun renderAspect(ms: MatrixStack, aspect: Aspect, x: Int, y: Int) {
        Minecraft.getInstance().textureManager.bind(ResourceLocation(aspect.id.namespace, "textures/aspects/" + aspect.id.path + ".png"))
        blitFullTexture(ms, x, y, 16, 16)
    }

    fun blitFullTexture(ms: MatrixStack, x: Int, y: Int, w: Int, h: Int) {
        blit(ms.last().pose(), x, x + w, y, y + h, 0, 0f, 1f, 0f, 1f)
    }

    fun blitOneToOne(ms: MatrixStack, x: Int, y: Int, w: Int, h: Int, texX: Int, texY: Int, atlasWidth: Int, atlasHeight: Int) {
        blit(ms, x, y, w, h, texX, texY, w, h, atlasWidth, atlasHeight)
    }

    //making texX and texY float might be useful to allow texX = Math.nextAfter(*, *). It can help to get rid of some rendering artifacts...
    fun blit(ms: MatrixStack, x: Int, y: Int, w: Int, h: Int, texX: Int, texY: Int, texW: Int, texH: Int, atlasWidth: Int, atlasHeight: Int) {
        blit(ms.last().pose(), x, x + w, y, y + h, 0, texX.toFloat() / atlasWidth, (texX.toFloat() + texW) / atlasWidth, texY.toFloat() / atlasHeight, (texY.toFloat() + texH) / atlasHeight)
    }

    private fun blit(pMatrix: Matrix4f, x1: Int, x2: Int, y1: Int, y2: Int, pBlitOffset: Int, u1: Float, u2: Float, v1: Float, v2: Float) {
        val bufferbuilder: BufferBuilder = Tessellator.getInstance().builder
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        bufferbuilder.vertex(pMatrix, x1.toFloat(), y2.toFloat(), pBlitOffset.toFloat()).uv(u1, v2).endVertex()
        bufferbuilder.vertex(pMatrix, x2.toFloat(), y2.toFloat(), pBlitOffset.toFloat()).uv(u2, v2).endVertex()
        bufferbuilder.vertex(pMatrix, x2.toFloat(), y1.toFloat(), pBlitOffset.toFloat()).uv(u2, v1).endVertex()
        bufferbuilder.vertex(pMatrix, x1.toFloat(), y1.toFloat(), pBlitOffset.toFloat()).uv(u1, v1).endVertex()
        bufferbuilder.end()
        RenderSystem.enableAlphaTest()
        WorldVertexBufferUploader.end(bufferbuilder)
    }


    fun hsbToRgb(hsb: Int): Int {
        val hue: Float = red(hsb) / 255f
        val saturation: Float = green(hsb) / 255f
        val brightness: Float = blue(hsb) / 255f
        var r = 0
        var g = 0
        var b = 0
        if (saturation == 0f) {
            b = (brightness * 255f + .5f).toInt()
            g = b
            r = g
        } else {
            val h = (hue - Math.floor(hue.toDouble()).toFloat()) * 6f
            val f = h - Math.floor(h.toDouble()).toFloat()
            val p = brightness * (1 - saturation)
            val q = brightness * (1 - saturation * f)
            val t = brightness * (1 - saturation * (1 - f))
            when (h.toInt()) {
                0 -> {
                    r = (brightness * 255f + .5f).toInt()
                    g = (t * 255 + .5).toInt()
                    b = (p * 255 + .5).toInt()
                }

                1 -> {
                    r = (q * 255 + .5).toInt()
                    g = (brightness * 255f + .5).toInt()
                    b = (p * 255 + .5).toInt()
                }

                2 -> {
                    r = (p * 255 + .5).toInt()
                    g = (brightness * 255f + .5).toInt()
                    b = (t * 255 + .5).toInt()
                }

                3 -> {
                    r = (p * 255 + .5).toInt()
                    g = (q * 255 + .5).toInt()
                    b = (brightness * 255f + .5).toInt()
                }

                4 -> {
                    r = (t * 255 + .5).toInt()
                    g = (p * 255 + .5).toInt()
                    b = (brightness * 255f + .5).toInt()
                }

                5 -> {
                    r = (brightness * 255f + .5).toInt()
                    g = (p * 255 + .5).toInt()
                    b = (q * 255 + .5).toInt()
                }
            }
        }
        return color(255, r, g, b)
    }

    fun rgbToHsb(color: Int): Int {
        val red: Int = red(color)
        val green: Int = green(color)
        val blue: Int = blue(color)
        var hue: Float
        val saturation: Float
        val brightness: Float
        var cMax: Int = max(red, green)
        if (blue > cMax) {
            cMax = blue
        }
        var cMin: Int = min(red, green)
        if (blue < cMin) {
            cMin = blue
        }
        brightness = cMax.toFloat()
        saturation = if (cMax != 0) (cMax - cMin) / cMax.toFloat() else 0f
        if (saturation == 0f)
            hue = 0f
        else {
            val redC = (cMax - red).toFloat() / (cMax - cMin).toFloat()
            val greenC = (cMax - green).toFloat() / (cMax - cMin).toFloat()
            val blueC = (cMax - blue).toFloat() / (cMax - cMin).toFloat()
            hue = if (red == cMax) blueC - greenC else if (green == cMax) 2 + redC - blueC else 4 + greenC - redC
            hue /= 6f
            if (hue < 0) hue++
        }
        return color(255, (hue * 255).toInt(), (saturation * 255).toInt(), brightness.toInt())
    }
}