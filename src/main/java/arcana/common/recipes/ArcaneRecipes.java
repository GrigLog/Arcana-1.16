package arcana.common.recipes;

import arcana.Arcana;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneRecipes {

    public static class Serializers{
        public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Arcana.id);

        public static final RegistryObject<IRecipeSerializer<WandsRecipe>> CRAFTING_WANDS = SERIALIZERS.register("wand", () -> new SpecialRecipeSerializer<>(WandsRecipe::new));
    }
}
