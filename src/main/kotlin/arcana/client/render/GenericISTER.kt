package arcana.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class GenericISTER<T : TileEntity>(val te: T) : ItemStackTileEntityRenderer() {
    override fun renderByItem(itemStackIn: ItemStack, transformType: TransformType, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int) {
        TileEntityRendererDispatcher.instance.renderItem(te, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn)
    }
}