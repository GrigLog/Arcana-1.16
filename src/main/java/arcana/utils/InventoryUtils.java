package arcana.utils;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public class InventoryUtils {
    public static void dropContents(World world, BlockPos pos, IItemHandler items){
        int size = items.getSlots();
        for (int i = 0; i < size; i++){
            InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
        }
    }

    public static void clear(IItemHandler items){
        int size = items.getSlots();
        for (int i = 0; i < size; i++){
            items.extractItem(i, items.getStackInSlot(i).getCount(), false);
        }
    }

    public static boolean stacksMatch(ItemStack a, ItemStack b){
        return a.getItem() == b.getItem() && tagsMatch(a.getTag(), b.getTag());
    }

    public static boolean tagsMatch(CompoundNBT a, CompoundNBT b){
        return Objects.equals(a, b);
    }
}
