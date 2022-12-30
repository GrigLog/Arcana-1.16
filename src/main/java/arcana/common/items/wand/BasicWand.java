package arcana.common.items.wand;

import arcana.common.capability.Mana;
import arcana.common.items.ArcanaGroup;
import arcana.common.items.spell.Spells;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import static arcana.utils.Util.arcLoc;

public class BasicWand extends Item implements MagicDevice {

    public static double DAMAGE = 10;
    public static double BURN_TIME = 3;

    public BasicWand(){ // Удалить потом
        super(ArcanaGroup.itemProps().rarity(Rarity.EPIC).stacksTo(1));
        setRegistryName(arcLoc("basicwand"));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (Mana.unchecked(player).tryConsume(new float[]{1, 1, 1, 1, 1, 1})) {
            Spells.FIRE_EXPLOSION.press(world, player);
        }
        return super.use(world, player, hand);
    }
}
