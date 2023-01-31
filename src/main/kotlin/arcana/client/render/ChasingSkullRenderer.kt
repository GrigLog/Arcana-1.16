package arcana.client.render

import arcana.common.entities.ChasingSkullEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.model.GenericHeadModel
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper

class ChasingSkullRenderer(manager: EntityRendererManager) : EntityRenderer<ChasingSkullEntity>(manager) {
    companion object {
        private val SKELETON_TEXTURE = ResourceLocation("textures/entity/skeleton/skeleton.png")
    }
    private val model = GenericHeadModel(0, 0, 64, 32)

    override fun getTextureLocation(pEntity: ChasingSkullEntity): ResourceLocation = SKELETON_TEXTURE

    override fun render(pEntity: ChasingSkullEntity, pEntityYaw: Float, pPartialTicks: Float, pMatrixStack: MatrixStack, pBuffer: IRenderTypeBuffer, pPackedLight: Int) {
        pMatrixStack.pushPose()
        pMatrixStack.scale(-1.0f, -1.0f, 1.0f)
        val yRot = MathHelper.rotlerp(pEntity.yRotO, pEntity.yRot, pPartialTicks)
        val xRot = MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot)

        //Arcana.logger.info("yRot:$yRot xRot:$xRot")
        val ivertexbuilder = pBuffer.getBuffer(model.renderType(getTextureLocation(pEntity)))
        model.setupAnim(0.0f, yRot, xRot)
        model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
        pMatrixStack.popPose()
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }
}