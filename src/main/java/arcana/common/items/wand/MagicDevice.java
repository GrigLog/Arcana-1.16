package arcana.common.items.wand;

import arcana.common.items.spell.Spell;
import arcana.common.items.spell.Spells;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface MagicDevice {
    default CoreItem getCore(ItemStack stack){
        String core_name = stack.getOrCreateTag().getString("core");
        return CoreItem.CORES.get(core_name);
    }

    default CapItem getCap(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("cap");
        return CapItem.CAPS.get(cap_name);
    }

    default FocusItem getFocus(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("focus");
        return FocusItem.FOCI.get(cap_name);
    }

    default Spell getSpell(ItemStack stack) {
        String spell_name = stack.getOrCreateTag().getString("spell");
        if (spell_name.isEmpty())
            return Spells.EMPTY;
        return Spells.REGISTRY.get(new ResourceLocation(spell_name));
    }
}
