package arcana.data;

import arcana.Arcana;
import arcana.common.items.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import javax.annotation.Nullable;

import static arcana.common.items.ModItemTags.*;

public class ItemTagsProvider extends net.minecraft.data.ItemTagsProvider {
    public ItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTags, @Nullable ExistingFileHelper helper) {
        super(gen, blockTags, Arcana.id, helper);
    }

    @Override
    protected void addTags() {
        tag(SCRIBING_TOOLS).add(ModItems.SCRIBING_TOOLS);
        tag(make("forge:ores/silver"));
        tag(make("forge:ingots/silver"));
        tag(make("forge:ingots/arcanium"));
    }

    private static Tags.IOptionalNamedTag<Item> make(String tag) {
        return ItemTags.createOptional(new ResourceLocation(tag));
    }
}
