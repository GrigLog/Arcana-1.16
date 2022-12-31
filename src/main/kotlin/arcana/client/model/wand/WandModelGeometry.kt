package arcana.client.model.wand

import arcana.common.items.ModItems
import arcana.common.items.wand.CapItem
import arcana.common.items.wand.CoreItem
import arcana.common.items.wand.WandItem
import arcana.utils.Util
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

class WandModelGeometry(// hold onto data here
    var cap: ResourceLocation?, var core: ResourceLocation?, var variant: ResourceLocation?, var focus: ResourceLocation?
) : IModelGeometry<WandModelGeometry?> {
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

        // get core texture
        val coreTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, core)
        // get cap texture
        val capTex = RenderMaterial(AtlasTexture.LOCATION_BLOCKS, cap)
        if (variant == null) variant = Util.arcLoc("wand")
        val coreLoc = ResourceLocation(variant!!.namespace, "item/wands/variants/" + variant!!.path)
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
        if (focus != null) {
            var focusModel =
                bakery.getBakedModel(Util.arcLoc("item/wands/foci/wand_focus"), modelTransform, spriteGetter)
            if (variant!!.path == "staff") focusModel =
                bakery.getBakedModel(Util.arcLoc("item/wands/foci/staff_focus"), modelTransform, spriteGetter)
            if (focusModel != null) builder.addAll(focusModel.getQuads(null, null, rand))
        }
        builder.addAll(coreModel.bake(bakery, spriteGetter, transformsFromModel, coreLoc)!!.getQuads(null, null, rand))
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
            var variant = Util.arcLoc("wand")
            if (stack.tag != null) variant = Util.arcLoc(stack.orCreateTag.getString("variant"))


            // get focus
            // nbt context comes from the focusData tag
            //FocusItem focus = WandItem.getFocus(stack);
            //CompoundNBT focusData =
            val capTex = if (cap != null) cap.registryName!! else ModItems.VOID_CAP.registryName!!
            val coreTex = if (core != null) core.registryName!! else ModItems.ICE_WAND_CORE.registryName!!
            return WandModelGeometry(
                capTex.withPath { "models/wands/caps/$it" },
                coreTex.withPath {"models/wands/cores/$it" },
                variant,
                Util.arcLoc("item/wands/foci/wand_focus")
            )
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