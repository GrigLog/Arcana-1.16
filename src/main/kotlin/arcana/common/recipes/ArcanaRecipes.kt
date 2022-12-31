package arcana.common.recipes

import arcana.utils.Util
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.SpecialRecipeSerializer

class ArcanaRecipes {
    object Serializers {
        @JvmField val CRAFTING_WANDS = withName(SpecialRecipeSerializer { id -> WandsRecipe(id) }, "wands")
        private fun <R : IRecipe<*>> withName(serializer: IRecipeSerializer<R>, regName: String): IRecipeSerializer<R> {
            return serializer.setRegistryName(Util.arcLoc(regName)) as IRecipeSerializer<R>
        }
    }
}