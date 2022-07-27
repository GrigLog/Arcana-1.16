package arcana.common.items.aspect;

import arcana.common.aspects.Aspect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Crystal extends Item {
    public final Aspect aspect;
    public Crystal(Properties props, Aspect aspect){
        super(props);
        this.aspect = aspect;
        setRegistryName(new ResourceLocation(aspect.id.getNamespace(), aspect.id.getPath() + "_crystal"));
    }

    @Override
    public ITextComponent getName(ItemStack pStack) {
        return new TranslationTextComponent("item.arcana.crystal", new TranslationTextComponent(aspect.translationKey));
    }
}
