package arcana.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.arcanamod.blocks.tiles.PedestalTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3f


class PedestalTileEntityRenderer(rendererDispatcher: TileEntityRendererDispatcher?) :
    TileEntityRenderer<PedestalTileEntity>(rendererDispatcher) {
    override fun render(tileEntity: PedestalTileEntity, partialTicks: Float, matrixStack: MatrixStack, buffer: IRenderTypeBuffer, combinedLight: Int, combinedOverlay: Int) {
        matrixStack.pushPose()

        val item: ItemStack = tileEntity.itemStack
        // translation above the pedestal + bobbing
        val bob = MathHelper.sin((tileEntity.level!!.gameTime.toFloat() + partialTicks) / 10.0f) * 0.1f
        matrixStack.translate(.5, (1.05f + bob).toDouble(), .5)
        // spin
        val spin = ((tileEntity.level!!.gameTime.toFloat() + partialTicks) / 20.0f)
        matrixStack.mulPose(Vector3f.YP.rotation(spin))
        Minecraft.getInstance().itemRenderer.renderStatic(item, ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer)

        matrixStack.popPose()
    }
}