package arcana.common.items;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import static arcana.utils.Util.arcLoc;

public class ModItemTags {
    public static Tags.IOptionalNamedTag<Item> SCRIBING_TOOLS = arcTag("scribing_tools");

    public static Tags.IOptionalNamedTag<Item> arcTag(String name){
        return ItemTags.createOptional(arcLoc(name));
    }
}
