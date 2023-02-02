package arcana.client.render

import arcana.common.entities.DamagingShovelEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f
import kotlin.math.PI

class DamagingShovelRenderer(manager: EntityRendererManager) : EntityRenderer<DamagingShovelEntity>(manager) {
    val itemRenderer = Minecraft.getInstance().itemRenderer

    override fun getTextureLocation(entity: DamagingShovelEntity): ResourceLocation? = AtlasTexture.LOCATION_BLOCKS

    override fun render(entity: DamagingShovelEntity, yaw: Float, partialTicks: Float, ms: MatrixStack, buffer: IRenderTypeBuffer?, packedLight: Int) {
        ms.pushPose()
        ms.scale(2f, 2f, 2f)
        ms.mulPose(Vector3f.YP.rotation((yaw * PI / 180 + PI / 2).toFloat()))
        val stack = ItemStack(Items.IRON_SHOVEL)
        val bakedModel = itemRenderer.getModel(stack, entity.level, null)
        val spin = entity.age + partialTicks
        ms.mulPose(Vector3f.ZN.rotation(spin))
        itemRenderer.render(stack, ItemCameraTransforms.TransformType.GROUND, false, ms, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel)
        ms.popPose()
        super.render(entity, yaw, partialTicks, ms, buffer, packedLight)
    }
}