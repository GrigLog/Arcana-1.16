package arcana.data;

import arcana.Arcana;
import arcana.common.items.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
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
    }
}
