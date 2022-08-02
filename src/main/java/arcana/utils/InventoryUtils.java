package arcana.utils;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtils {
    public static void dropContents(World world, BlockPos pos, IItemHandler items){
        int size = items.getSlots();
        for (int i = 0; i < size; i++){
            InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
        }
    }
}
