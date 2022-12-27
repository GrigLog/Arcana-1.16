package arcana.common.items;

import arcana.Arcana;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.SeparatePerspectiveModel;

import static arcana.utils.Util.arcLoc;

public class ArcanumItem extends Item {

    public ArcanumItem() {
        super(new Properties().tab(Arcana.ARCANAGROUP).stacksTo(1));
        setRegistryName(arcLoc("arcanum"));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            CompoundNBT nbt = new CompoundNBT();
            ItemStack stack = new ItemStack(this.getItem(), 1);
            nbt.putInt("open", 0);
            stack.setTag(nbt);
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        CompoundNBT tag = player.getItemInHand(hand).getOrCreateTag();
        tag.putBoolean("open", !tag.getBoolean("open"));
        return super.use(world, player, hand);
    }
}
