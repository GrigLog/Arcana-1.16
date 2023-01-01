package arcana.client.model.wand

import arcana.common.items.ModItems
import arcana.common.items.wand.CapItem
import arcana.common.items.wand.CoreItem
import arcana.common.items.wand.WandItem
import arcana.utils.Util.arcLoc
import arcana.utils.Util.withPath
import com.google.common.collect.ImmutableList
import com.google.common.collect.Maps
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.client.model.ModelTransformComposition
import net.minecraftforge.client.model.PerspectiveMapWrapper
import net.minecraftforge.client.model.geometry.IModelGeometry
import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.function.Function

//unbaked model
class WandModelGeometry(val capid: ResourceLocation,
                        val coreId: ResourceLocation,
                        val variant: ResourceLocation)
    : IModelGeometry<WandModelGeometry?> {
    constructor() : this(ModItems.VOID_CAP.id,  ModItems.ICE_CORE.id, arcLoc("wand"))

    var LOGGER = LogManager.getLogger()
    override fun bake(
        owner: IModelConfiguration,
        bakery: ModelBakery,
        spriteGetter: Function<RenderMaterial, TextureAtlasSprite>,
        modelTransform: IModelTransform,
        overrides: ItemOverrideList,
        modelLocation: ResourceLocation
    ): IBakedModel {
        val transformsFromModel = owner.combinedTransform
        val builder = ImmutableList.builder<BakedQuad>()
        val transformMap =
            PerspectiveMapWrapper.getTransforms(ModelTransformComposition(transformsFromModel, modelTransform))

        val coreTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, capid.withPath{"models/wands/caps/$it"})
        val capTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, coreId.withPath{"models/wands/cores/$it"})
        val corePath = variant.withPath{"item/wands/variants/$it"}
        val focusPath = variant.withPath{"item/wands/foci/${it}_focus"}
        val coreModel = bakery.getModel(corePath)
        val coreBakedModel = coreModel.bake(bakery, spriteGetter, transformsFromModel, corePath)!!
        val focusModelBaked = bakery.getBakedModel(focusPath, modelTransform, spriteGetter)

        var tfs = ItemCameraTransforms.NO_TRANSFORMS
        // they *should* be, but might as well check.
        if (coreModel is BlockModel) {
            val model = coreModel
            model.textureMap["core"] = Either.left(coreTex)
            model.textureMap["cap"] = Either.left(capTex)
            tfs = model.transforms
        } else {
            LOGGER.error("Wand model isn't a block model!")
        }

        val rand = Random()
        if (focusModelBaked != null)
            builder.addAll(focusModelBaked.getQuads(null, null, rand))
        builder.addAll(coreBakedModel.getQuads(null, null, rand))
        return WandBakedModel(
            builder.build(),
            spriteGetter.apply(coreTex),
            Maps.immutableEnumMap(transformMap),
            AttachmentOverrideHandler(bakery),
            true,
            true,
            this,
            owner,
            modelTransform,
            tfs
        )
    }

    override fun getTextures(owner: IModelConfiguration,
        modelGetter: Function<ResourceLocation, IUnbakedModel>,
        missingTextureErrors: Set<Pair<String, String>>
    ): Collection<RenderMaterial> {
        return emptyList()
    }

    protected class AttachmentOverrideHandler(private val bakery: ModelBakery) : ItemOverrideList() {
        override fun resolve(originalModel: IBakedModel, stack: ItemStack, world: ClientWorld?, entity: LivingEntity?): IBakedModel {
            // get cap
            val cap: CapItem = WandItem.getCap(stack)
            // get material
            val core: CoreItem = WandItem.getCore(stack)
            // get variant (staff/scepter/wand)
            // TODO: improve this slightly
            var variant = arcLoc("wand")
            if (stack.tag != null)
                variant = arcLoc(stack.tag!!.getString("variant"))


            return WandModelGeometry(cap.id, core.id, variant)
                .bake(
                    (originalModel as WandBakedModel).owner,
                    bakery,
                    ModelLoader.defaultTextureGetter(),
                    originalModel.modelTransform,
                    originalModel.getOverrides(),
                    ResourceLocation("arcana:does_this_do_anything_lol")
                )
        }
    }
}