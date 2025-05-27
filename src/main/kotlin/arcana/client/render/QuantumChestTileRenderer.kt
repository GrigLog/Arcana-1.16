package arcana.client.render

import arcana.common.blocks.ModBlocks
import arcana.common.blocks.QuantumChestBlock
import arcana.common.blocks.tiles.QuantumChestTile
import arcana.utils.Util
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import it.unimi.dsi.fastutil.floats.Float2FloatFunction
import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.ChestBlock.FACING
import net.minecraft.client.renderer.Atlases
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.client.renderer.model.RenderMaterial
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.tileentity.IChestLid
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.World


class QuantumChestTileRenderer<T>(tileEntityRendererDispatcher: TileEntityRendererDispatcher?) :
    TileEntityRenderer<T>(tileEntityRendererDispatcher) where T : TileEntity?, T : IChestLid? {
    companion object {
        val QUANTUM_CHEST_TEXTURE = Util.arcLoc("block/quantum_chest")
    }
    private val chestLid: ModelRenderer
    private val chestBottom = ModelRenderer(64, 64, 0, 19)
    private val chestLock: ModelRenderer

    init {
        chestBottom.addBox(1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f, 0.0f)
        chestLid = ModelRenderer(64, 64, 0, 0)
        chestLid.addBox(1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f, 0.0f)
        chestLid.y = 9.0f
        chestLid.z = 1.0f
        chestLock = ModelRenderer(64, 64, 0, 0)
        chestLock.addBox(7.0f, -1.0f, 15.0f, 2.0f, 4.0f, 1.0f, 0.0f)
        chestLock.y = 8.0f
    }

    override fun render(tileEntity: T, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int) {
        val tileEntity = tileEntity as QuantumChestTile
        val block = ModBlocks.QUANTUM_CHEST as QuantumChestBlock

        val level: World? = tileEntity.level
        val inLevel = level != null

        val blockstate: BlockState = if (inLevel) tileEntity.getBlockState()
            else block.defaultBlockState().setValue(FACING, Direction.SOUTH)

        matrixStackIn.pushPose()
        val f: Float = blockstate.getValue(FACING).toYRot()
        matrixStackIn.translate(0.5, 0.5, 0.5)
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f))
        matrixStackIn.translate(-0.5, -0.5, -0.5)

        val iCallbackWrapper = block.combine(blockstate, level, tileEntity.blockPos, true)
        var f1: Float = iCallbackWrapper.apply<Float2FloatFunction>(ChestBlock.opennessCombiner(tileEntity)).get(partialTicks)

        f1 = 1.0f - f1
        f1 = 1.0f - f1 * f1 * f1
        val i = iCallbackWrapper.apply(DualBrightnessCallback<TileEntity?>()).applyAsInt(combinedLightIn)

        val material = RenderMaterial(Atlases.CHEST_SHEET, QUANTUM_CHEST_TEXTURE)
        val ivertexbuilder: IVertexBuilder = material.buffer(bufferIn, RenderType::entityCutout)

        this.handleModelRender(matrixStackIn, ivertexbuilder, chestLid, chestLock, chestBottom, f1, i, combinedOverlayIn)

        matrixStackIn.popPose()
    }

    private fun handleModelRender(matrixStackIn: MatrixStack, iVertexBuilder: IVertexBuilder, chestLid: ModelRenderer, chestLock: ModelRenderer, chestBottom: ModelRenderer, f1: Float, packedLight: Int, packedOverlay: Int) {
        chestLid.xRot = -(f1 * (Math.PI.toFloat() / 2f))
        chestLock.xRot = chestLid.xRot
        chestLid.render(matrixStackIn, iVertexBuilder, packedLight, packedOverlay)
        chestLock.render(matrixStackIn, iVertexBuilder, packedLight, packedOverlay)
        chestBottom.render(matrixStackIn, iVertexBuilder, packedLight, packedOverlay)
    }
}