package arcana.common.items;

import arcana.Arcana;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static arcana.utils.Util.arcLoc;

public class CapItem extends Item {
    public static Map<String, CapItem> CAPS = new LinkedHashMap<>();
    public final int complexity, visStorage, level;
    public String name;

    public CapItem(String name, int visStorage, int complexity, int level){
        super(new Properties().tab(Arcana.ARCANAGROUP));
        setRegistryName(arcLoc(name));
        this.complexity = complexity;
        this.visStorage = visStorage;
        this.level = level;
        this.name = name;
        CAPS.put(name,this);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(new StringTextComponent(String.format("VisStorage: %s",visStorage)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Level: %s",level)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Complexity: %s",complexity)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
    }
}
