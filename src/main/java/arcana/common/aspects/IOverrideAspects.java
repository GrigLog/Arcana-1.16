package arcana.common.aspects;

import net.minecraft.item.ItemStack;

public interface IOverrideAspects {
    AspectList get(ItemStack is, AspectList original);
}
