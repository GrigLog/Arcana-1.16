package arcana.common.items;

import arcana.Arcana;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static arcana.utils.Util.arcLoc;

public class WandItem extends Item {

    public WandItem(){
        super(new Properties().tab(Arcana.ARCANAGROUP).stacksTo(1));
        setRegistryName(arcLoc("wand"));
    }

    public ItemStack withCapAndCore(CapItem cap, CoreItem core){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("cap", cap.name);
        nbt.putString("core", core.name);
        ItemStack stack = new ItemStack(ModItems.Wand.asItem(), 1);
        stack.setTag(nbt);
        return stack;
    }

    public static CoreItem getCore(ItemStack stack){
        String core_name = stack.getOrCreateTag().getString("core");
        return CoreItem.CORES.get(core_name);
    }

    public static CapItem getCap(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("cap");
        return CapItem.CAPS.get(cap_name);
    }

    public static FocusItem getFocus(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("focus");
        return FocusItem.FOCI.get(cap_name);
    }


    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(allowdedIn(group)){
            // iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
            items.add(withCapAndCore(ModItems.IronCap, ModItems.WOOD_WAND_CORE));
            items.add(withCapAndCore(ModItems.SilverCap, ModItems.DAIR_WAND_CORE));
            items.add(withCapAndCore(ModItems.GoldCap, ModItems.GREATWOOD_WAND_CORE));
            items.add(withCapAndCore(ModItems.ThaumiumCap, ModItems.SILVERWOOD_WAND_CORE));
            items.add(withCapAndCore(ModItems.VoidCap,ModItems.ARCANIUM_WAND_CORE));
            items.add(withCapAndCore(ModItems.VoidCap,ModItems.BLAZE_WAND_CORE));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
        super.appendHoverText(stack, world, tooltip, flag);
        CoreItem core = getCore(stack);
        CapItem cap = getCap(stack);
        tooltip.add(new StringTextComponent(String.format("MaxVis: %s",core.maxVis+cap.visStorage)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Level: %s",core.level+cap.level)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Difficulty: %s",core.difficulty)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Complexity: %s",cap.complexity)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
    }
}
