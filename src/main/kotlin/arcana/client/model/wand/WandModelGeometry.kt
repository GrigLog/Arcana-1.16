
import arcana.client.ClientPaths.CAPS_3D
import arcana.client.ClientPaths.CORES_3D
import arcana.client.model.wand.WandBakedModel
import arcana.common.items.ModItems
import arcana.common.items.wand.WandItem
import arcana.utils.Util.arcLoc
import arcana.utils.Util.withPath
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.client.model.ModelTransformComposition
import net.minecraftforge.client.model.PerspectiveMapWrapper
import net.minecraftforge.client.model.geometry.IModelGeometry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*
import java.util.function.Function


class WandModelGeometry(
    val cap: ResourceLocation,
    val core: ResourceLocation,
    val variant: ResourceLocation,
    var focus: ResourceLocation
) : IModelGeometry<WandModelGeometry> {

    constructor() : this(ModItems.VOID_CAP.id, ModItems.ICE_CORE.id, arcLoc("wand"), arcLoc("no_focus"))

    var LOGGER: Logger = LogManager.getLogger()
    override fun bake(owner: IModelConfiguration, bakery: ModelBakery,
        spriteGetter: java.util.function.Function<RenderMaterial, TextureAtlasSprite>,
        modelTransform: IModelTransform?, overrides: ItemOverrideList?, modelLocation: ResourceLocation?
    ): IBakedModel {
        val transformsFromModel = owner.combinedTransform
        val builder = ImmutableList.builder<BakedQuad>()
        val transformMap: ImmutableMap<TransformType, TransformationMatrix> =
            PerspectiveMapWrapper.getTransforms(ModelTransformComposition(transformsFromModel, modelTransform))

        // get core texture
        val coreTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, core)
        // get cap texture
        val capTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, cap)
        val coreLoc = variant.withPath{"item/wands/variants/" + it }
        //ResourceLocation coreLoc = arcLoc("item/wands/variants/wand");
        val coreModel = bakery.getModel(coreLoc)
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

        // get focus model and texture, apply, and add
        val rand = Random()
        var focusModel = bakery.getBakedModel(arcLoc("item/wands/foci/wand_focus"), modelTransform, spriteGetter)
        if (variant.path == "staff") focusModel =
            bakery.getBakedModel(arcLoc("item/wands/foci/staff_focus"), modelTransform, spriteGetter)
        if (focusModel != null)
            builder.addAll(focusModel.getQuads(null, null, rand))
        builder.addAll(coreModel.bake(bakery, spriteGetter, transformsFromModel, coreLoc)!!.getQuads(null, null, rand))
        return WandBakedModel(builder.build(), spriteGetter.apply(coreTex), Maps.immutableEnumMap(transformMap),
                              AttachmentOverrideHandler(bakery), true, true, this,
                              owner, modelTransform!!, tfs)
    }

    override fun getTextures(owner: IModelConfiguration, modelGetter: Function<ResourceLocation, IUnbakedModel>, missingTextureErrors: MutableSet<Pair<String, String>>): MutableCollection<RenderMaterial> {
        return Collections.emptyList()
    }

    protected class AttachmentOverrideHandler(private val bakery: ModelBakery) : ItemOverrideList() {
        override fun resolve(originalModel: IBakedModel, stack: ItemStack, world: ClientWorld?, entity: LivingEntity?): IBakedModel? {
            // get cap
            val cap = WandItem.getCap(stack)
            // get material
            val core = WandItem.getCore(stack)
            // get variant (staff/scepter/wand)
            // TODO: improve this slightly
            var variant: ResourceLocation = arcLoc("wand")
            if (stack.tag != null)
                variant = arcLoc(stack.orCreateTag.getString("variant"))


            // get focus
            // nbt context comes from the focusData tag
            //FocusItem focus = WandItem.getFocus(stack);
            //CompoundNBT focusData =
            val capTex = cap.id
            val coreTex = core.id
            return WandModelGeometry(
                capTex.withPath { CAPS_3D + it },
                coreTex.withPath { CORES_3D + it },
                variant,
                arcLoc("item/wands/foci/wand_focus"))
                .bake(
                    (originalModel as WandBakedModel).owner,
                    bakery,
                    ModelLoader.defaultTextureGetter(),
                    originalModel.modelTransform,
                    originalModel.getOverrides(),
                    ResourceLocation("arcana:does_this_do_anything_lol"))
        }
    }
}