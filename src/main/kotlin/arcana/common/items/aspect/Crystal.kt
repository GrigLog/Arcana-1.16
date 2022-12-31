package arcana.common.items.aspect

import arcana.common.aspects.Aspect
import arcana.utils.Util.withPath
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TranslationTextComponent

class Crystal(props: Properties, val aspect: Aspect) : Item(props) {
    init {
        registryName = aspect.id.withPath{it+"_crystal"}
    }

    override fun getName(pStack: ItemStack) =
        TranslationTextComponent("item.arcana.crystal", TranslationTextComponent(aspect.translationKey))

}