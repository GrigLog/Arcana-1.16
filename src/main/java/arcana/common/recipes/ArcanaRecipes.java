package arcana.common.recipes;

import arcana.Arcana;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static arcana.utils.Util.arcLoc;

@SuppressWarnings("unchecked")
public class ArcanaRecipes {

    public static class Serializers{
        public static IRecipeSerializer<WandsRecipe> CRAFTING_WANDS = withName(new SpecialRecipeSerializer<>(WandsRecipe::new), "wands");


        private static <T extends IRecipe<?>> IRecipeSerializer<T> withName(IRecipeSerializer<T> serializer, String regName){
            return (IRecipeSerializer<T>) serializer.setRegistryName(arcLoc(regName));
        }
    }
}
