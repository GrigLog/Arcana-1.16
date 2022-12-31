package arcana.common.aspects

import arcana.common.items.ArcanaGroup
import arcana.common.items.aspect.AspectIcon
import arcana.common.items.aspect.Crystal
import arcana.utils.Pair
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.registries.IForgeRegistry
import java.util.stream.Collectors

object AspectUtils {
    val aspectItems: List<Item> = ArrayList()
    val aspectCrystalItems: Map<Aspect, Item> = HashMap()
    var aspectStacks: List<ItemStack>? = null
    fun registerItems(registry: IForgeRegistry<Item?>) {
        for (aspect in Aspects.list)
            if (aspect !== Aspects.EMPTY) {
                val item = AspectIcon(aspect)
                val crystal: Item = Crystal(ArcanaGroup.props, aspect)
                registry.registerAll(item, crystal)
            }
        aspectStacks = aspectItems.stream().map { ItemStack(it) }
            .collect(Collectors.toList())
    }

    fun areAspectsConnected(a: Aspect, b: Aspect): Boolean {
        return Aspects.COMBINATIONS.inverse().getOrDefault(a, Pair.of(null, null)).contains(b)
            || Aspects.COMBINATIONS.inverse().getOrDefault(b, Pair.of(null, null)).contains(a)
    }

    fun add(aspects: MutableCollection<AspectStack>, a: Aspect, amount: Int) {
        for (ass in aspects) {
            if (ass.aspect === a) {
                ass.amount += amount
                return
            }
        }
        aspects.add(AspectStack(a, amount))
    }

    fun add(aspects: MutableCollection<AspectStack>, a: AspectStack) {
        for (ass in aspects) {
            if (ass.aspect === a.aspect) {
                ass.amount += a.amount
                return
            }
        }
        aspects.add(a)
    }

    fun add(list1: MutableCollection<AspectStack>, list2: Collection<AspectStack>) {
        for (ass in list2) {
            add(list1, ass)
        }
    }
}