package arcana.common.research.requirements;

import arcana.common.research.Requirement;
import arcana.utils.InventoryUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRequirement implements Requirement {
    ItemStack is;

    @Override
    public boolean satisfied(PlayerEntity player) {
        int count = ItemStackHelper.clearOrCountMatchingItems(player.inventory,
            (stack) -> InventoryUtils.stacksMatch(is, stack),
            is.getCount(), true);
        return count >= is.getCount();
    }

    @Override
    public void take(PlayerEntity player) {
        ItemStackHelper.clearOrCountMatchingItems(player.inventory,
            (stack) -> InventoryUtils.stacksMatch(is, stack), is.getCount(), false);
    }

    @Override
    public void deserialize(String data) {
        String[] parts = data.split("\\*");
        int count = Integer.parseInt(parts[0]);
        int tagStart = parts[1].indexOf('{');
        if (tagStart == -1){
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[1]));
            is = new ItemStack(item, count);
            return;
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[1].substring(0, tagStart)));
        CompoundNBT tag = null;
        try {
            tag = JsonToNBT.parseTag(parts[1].substring(tagStart));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        is = new ItemStack(item, count, tag);
    }
}
