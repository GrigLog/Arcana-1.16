package arcana.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ResearchNote extends Item {
    boolean complete;
    public ResearchNote(Properties props, boolean complete) {
        super(props);
        this.complete = complete;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!complete)
            return super.use(world, player, hand);
        //give research
        return super.use(world, player, hand);
    }
}
