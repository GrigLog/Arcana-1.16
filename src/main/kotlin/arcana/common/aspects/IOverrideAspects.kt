package arcana.common.aspects

import net.minecraft.item.ItemStack

interface IOverrideAspects {
    fun getAspects(stack: ItemStack, original: AspectList): AspectList
}