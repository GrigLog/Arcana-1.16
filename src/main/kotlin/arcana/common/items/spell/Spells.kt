package arcana.common.items.spell

import arcana.utils.Util
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import net.minecraft.util.ResourceLocation

object Spells {
    var REGISTRY: BiMap<ResourceLocation, Spell> = HashBiMap.create()

    val EMPTY = register("name", Spell())
    val FIREMELON = register("firemelon", FireMelon(3f, 3, 4))
    val SPRAY = register("spray", WaterSpray())
    val CHASING_SKULL = register("chasing_skull", ChasingSkull())
    val RANDOM_POTION = register("random_potion", RandomPotion())
    val DAMAGING_SHOVEL = register("damaging_shovel", DamagingShovel())
    val QUICK_WALL = register("quick_wall", QuickWall())

    private fun register(name: String, spell: Spell): Spell {
        REGISTRY[Util.arcLoc(name)] = spell
        return spell
    }
}