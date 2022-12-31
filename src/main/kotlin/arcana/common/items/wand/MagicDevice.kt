package arcana.common.items.wand

import arcana.common.items.spell.Spell
import arcana.common.items.spell.Spells
import arcana.utils.Util.fromId
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

interface MagicDevice {
    fun getCore(stack: ItemStack): CoreItem {
        return fromId(CoreItem.CORES, stack.getOrCreateTag().getString("core"))!!
    }

    fun getCap(stack: ItemStack): CapItem {
        return fromId(CapItem.CAPS, stack.getOrCreateTag().getString("cap"))!!
    }

    fun getFocus(stack: ItemStack): FocusItem {
        return fromId(FocusItem.FOCI, stack.getOrCreateTag().getString("focus"))!!
    }

    fun getSpell(stack: ItemStack): Spell {
        val spell_name = stack.orCreateTag.getString("spell")
        return if (spell_name.isEmpty())
            Spells.EMPTY
        else
            Spells.REGISTRY[ResourceLocation(spell_name)]!!
    }
}