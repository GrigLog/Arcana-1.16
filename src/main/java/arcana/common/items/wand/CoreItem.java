package arcana.common.items.wand;

import arcana.Arcana;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

public class CoreItem extends Item {
    public static Map<ResourceLocation, CoreItem> CORES = new LinkedHashMap<>();
    public final int maxVis,difficulty,level;
    public ResourceLocation id;

    public CoreItem(ResourceLocation id, int maxVis, int difficulty, int level) {
        super(new Properties().tab(Arcana.ARCANAGROUP));
        setRegistryName(id);
        this.maxVis = maxVis;
        this.level = level;
        this.difficulty = difficulty;
        this.id = id;
        CORES.put(id, this);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
        super.appendHoverText(stack, world, tooltip, flag);
            tooltip.add(new StringTextComponent(String.format("MaxVis: %s",maxVis)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
            tooltip.add(new StringTextComponent(String.format("Level: %s",level)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
            tooltip.add(new StringTextComponent(String.format("Difficulty: %s",difficulty)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
    }

    public boolean capAllowed(CapItem cap){
        return level >= cap.level;
    }

    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(id.getNamespace(), "models/wands/materials/" + id.getPath());
    }

    public ResourceLocation getGuiTexture(){
        return new ResourceLocation(id.getNamespace(), "textures/gui/hud/wand/core/" + id.getPath() + ".png");
    }
}
