package arcana.common.research.requirements;

import arcana.common.research.Requirement;
import arcana.utils.InventoryUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class TagRequirement implements Requirement {
    Tags.IOptionalNamedTag<Item> tag;
    int count;
    CompoundNBT nbt;

    @Override
    public boolean satisfied(PlayerEntity player) {
        int found = ItemStackHelper.clearOrCountMatchingItems(player.inventory,
            (stack) -> tag.contains(stack.getItem()) && InventoryUtils.tagsMatch(stack.getTag(), nbt),
            count, true);
        return found >= count;
    }

    @Override
    public void take(PlayerEntity player) {
        ItemStackHelper.clearOrCountMatchingItems(player.inventory,
            (stack) -> tag.contains(stack.getItem()) && InventoryUtils.tagsMatch(stack.getTag(), nbt),
            count, false);
    }

    @Override
    public void deserialize(String data) {
        String[] parts = data.split("\\*");
        count = Integer.parseInt(parts[0]);
        int tagStart = parts[1].indexOf('{');
        if (tagStart == -1){
            tag = ItemTags.createOptional(new ResourceLocation(parts[1]));
            return;
        }
        tag = ItemTags.createOptional(new ResourceLocation(parts[1].substring(0, tagStart)));
        try {
            nbt = JsonToNBT.parseTag(parts[1].substring(tagStart));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
