package arcana.client.render

import arcana.common.entities.AspectOrbEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ColorHelper.PackedColor
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3f

class AspectOrbRenderer(manager: EntityRendererManager) : EntityRenderer<AspectOrbEntity>(manager) {
    companion object {
        val EXPERIENCE_ORB_LOCATION: ResourceLocation = ResourceLocation("textures/entity/experience_orb.png")
        val RENDER_TYPE: RenderType = RenderType.itemEntityTranslucentCull(EXPERIENCE_ORB_LOCATION)
    }

    override fun getTextureLocation(pEntity: AspectOrbEntity): ResourceLocation = EXPERIENCE_ORB_LOCATION

    override fun render(pEntity: AspectOrbEntity, pEntityYaw: Float, pPartialTicks: Float, pMatrixStack: MatrixStack, pBuffer: IRenderTypeBuffer, pPackedLight: Int) {
        pMatrixStack.pushPose()
        val size: Int = pEntity.getIcon()
        val u1 = (size % 4 * 16 + 0).toFloat() / 64.0f
        val u2 = (size % 4 * 16 + 16).toFloat() / 64.0f
        val v1 = (size / 4 * 16 + 0).toFloat() / 64.0f
        val v2 = (size / 4 * 16 + 16).toFloat() / 64.0f
        val renderTicks = (pEntity.tickCount.toFloat() + pPartialTicks) / 2.0f
        pMatrixStack.translate(0.0, 0.1, 0.0)
        pMatrixStack.mulPose(entityRenderDispatcher.cameraOrientation())
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0f))
        pMatrixStack.scale(0.3f, 0.3f, 0.3f)
        val ivertexbuilder = pBuffer.getBuffer(RENDER_TYPE)
        val `matrixstack$entry` = pMatrixStack.last()
        val matrix4f = `matrixstack$entry`.pose()
        val matrix3f = `matrixstack$entry`.normal()
        /*val red = ((MathHelper.sin(renderTicks + 0.0f) + 1.0f) * 0.5f * 255.0f).toInt()
        val green = 255
        val blue = ((MathHelper.sin(renderTicks + 4.1887903f) + 1.0f) * 0.1f * 255.0f).toInt()*/
        val packedColor = pEntity.aspectStack.aspect.colors[1]
        val red = PackedColor.red(packedColor)
        val green = PackedColor.green(packedColor)
        val blue = PackedColor.blue(packedColor)
        vertex(ivertexbuilder, matrix4f, matrix3f, -0.5f, -0.25f, red, green, blue, u1, v2, pPackedLight)
        vertex(ivertexbuilder, matrix4f, matrix3f, 0.5f, -0.25f, red, green, blue, u2, v2, pPackedLight)
        vertex(ivertexbuilder, matrix4f, matrix3f, 0.5f, 0.75f, red, green, blue, u2, v1, pPackedLight)
        vertex(ivertexbuilder, matrix4f, matrix3f, -0.5f, 0.75f, red, green, blue, u1, v1, pPackedLight)
        pMatrixStack.popPose()
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }

    private fun vertex(pBuffer: IVertexBuilder, pMatrix: Matrix4f, pMatrixNormal: Matrix3f, pX: Float, pY: Float, pRed: Int, pGreen: Int, pBlue: Int, pTexU: Float, pTexV: Float, pPackedLight: Int) {
        pBuffer.vertex(pMatrix, pX, pY, 0.0f).color(pRed, pGreen, pBlue, 255).uv(pTexU, pTexV)
            .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(pMatrixNormal, 0.0f, 1.0f, 0.0f)
            .endVertex()
    }
}