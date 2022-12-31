package arcana.common.items.aspect

import arcana.common.aspects.Aspect
import net.minecraft.item.Item

class AspectIcon(var aspect: Aspect) : Item(Properties()) {
    init {
        registryName = aspect.id
    }
    override fun getDescriptionId() = aspect.translationKey
}