package arcana.client.model.wand

import WandModelGeometry
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.model.IModelTransform
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraftforge.client.model.BakedItemModel
import net.minecraftforge.client.model.IModelConfiguration

class WandBakedModel(
    quads: ImmutableList<BakedQuad>,
    particle: TextureAtlasSprite,
    transforms: ImmutableMap<TransformType, TransformationMatrix>,
    overrides: ItemOverrideList,
    untransformed: Boolean,
    isSideLit: Boolean,
    val parent: WandModelGeometry,
    var owner: IModelConfiguration,
    var modelTransform: IModelTransform,
    var itemTransforms: ItemCameraTransforms?
) : BakedItemModel(quads, particle, transforms, overrides, untransformed, isSideLit) {
    override fun getTransforms(): ItemCameraTransforms {
        return if (itemTransforms != null) itemTransforms!! else ItemCameraTransforms.NO_TRANSFORMS
    }

    val itemCameraTransforms: ItemCameraTransforms
        get() = if (itemTransforms != null) itemTransforms!! else ItemCameraTransforms.NO_TRANSFORMS
}