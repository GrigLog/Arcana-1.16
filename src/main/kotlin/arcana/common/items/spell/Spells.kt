package arcana.common.items.spell

import arcana.utils.Util
import net.minecraft.util.ResourceLocation

object Spells {
    var REGISTRY: MutableMap<ResourceLocation, Spell> = HashMap()
    val EMPTY: Spell = register("name", Spell())
    val FIREMELON: Spell = register("firemelon", FireMelon(3f, 3, 4))

    private fun register(name: String, spell: Spell): Spell {
        REGISTRY[Util.arcLoc(name)] = spell
        return spell
    }
}