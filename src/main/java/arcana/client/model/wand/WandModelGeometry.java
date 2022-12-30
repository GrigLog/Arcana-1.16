package arcana.client.model.wand;

import arcana.common.items.wand.CapItem;
import arcana.common.items.wand.CoreItem;
import arcana.common.items.wand.WandItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static arcana.utils.Util.arcLoc;
import static arcana.utils.Util.withPath;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class WandModelGeometry implements IModelGeometry<WandModelGeometry> {
    // hold onto data here
    ResourceLocation cap;
    ResourceLocation core;
    ResourceLocation variant;
    ResourceLocation focus;

    Logger LOGGER = LogManager.getLogger();

    public WandModelGeometry(ResourceLocation cap, ResourceLocation core, ResourceLocation variant, ResourceLocation focus) {
        this.cap = cap;
        this.core = core;
        this.variant = variant;
        this.focus = focus;
    }

    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        IModelTransform transformsFromModel = owner.getCombinedTransform();
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap = PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));

        // get core texture
        RenderMaterial coreTex = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, core);
        // get cap texture
        RenderMaterial capTex = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, cap);

        if(variant == null)
            variant = arcLoc("wand");

        ResourceLocation coreLoc = new ResourceLocation(variant.getNamespace(), "item/wands/variants/" + variant.getPath());
        //ResourceLocation coreLoc = arcLoc("item/wands/variants/wand");
        IUnbakedModel coreModel = bakery.getModel(coreLoc);
        ItemCameraTransforms tfs = ItemCameraTransforms.NO_TRANSFORMS;

        // they *should* be, but might as well check.
        if (coreModel instanceof BlockModel) {
            BlockModel model = (BlockModel) coreModel;
            model.textureMap.put("core", Either.left(coreTex));
            model.textureMap.put("cap", Either.left(capTex));
            tfs = model.getTransforms();
        } else {
            LOGGER.error("Wand model isn't a block model!");
        }

        // get focus model and texture, apply, and add
        Random rand = new Random();
        if (focus != null) {
                IBakedModel focusModel = bakery.getBakedModel(arcLoc("item/wands/foci/wand_focus"), modelTransform, spriteGetter);
            if(variant.getPath().equals("staff")) focusModel = bakery.getBakedModel(arcLoc("item/wands/foci/staff_focus"), modelTransform, spriteGetter);
            if (focusModel != null)
                builder.addAll(focusModel.getQuads(null, null, rand));
        }

        builder.addAll(coreModel.bake(bakery, spriteGetter, transformsFromModel, coreLoc).getQuads(null, null, rand));

        return new WandBakedModel(builder.build(), spriteGetter.apply(coreTex), Maps.immutableEnumMap(transformMap), new AttachmentOverrideHandler(bakery), true, true, this, owner, modelTransform, tfs);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.emptyList();
    }

    protected static final class AttachmentOverrideHandler extends ItemOverrideList {
        private final ModelBakery bakery;

        public AttachmentOverrideHandler(ModelBakery bakery) {
            this.bakery = bakery;
        }


        @Nullable
        @Override
        public IBakedModel resolve(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
            // get cap
            CapItem cap = WandItem.getCap(stack);
            // get material
            CoreItem core = WandItem.getCore(stack);
            // get variant (staff/scepter/wand)
            // TODO: improve this slightly
            ResourceLocation variant = arcLoc("wand");
            if(stack.getTag() != null)
                variant = arcLoc(stack.getOrCreateTag().getString("variant"));


            // get focus
            // nbt context comes from the focusData tag
            //FocusItem focus = WandItem.getFocus(stack);
            //CompoundNBT focusData =
            ResourceLocation capTex = cap!=null ? cap.id : arcLoc("void_cap");
            ResourceLocation coreTex = core!=null ? core.id : arcLoc("ice_wand_core");

            return new WandModelGeometry(
                    withPath(capTex, "models/wands/caps/"),
                    withPath(coreTex,"models/wands/cores/"),
                    variant,
                    arcLoc("item/wands/foci/wand_focus"))
                    .bake(
                            ((WandBakedModel) originalModel).owner,
                            bakery,
                            ModelLoader.defaultTextureGetter(),
                            ((WandBakedModel) originalModel).modelTransform,
                            originalModel.getOverrides(),
                            new ResourceLocation("arcana:does_this_do_anything_lol"));
        }
    }
}