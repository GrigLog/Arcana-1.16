package arcana.common.items;

import arcana.Arcana;
import arcana.common.FireExplosion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BasicWand extends Item {

    public static double DAMAGE = 10;
    public static double BURN_TIME = 3;

    public BasicWand(){
        super(new Item.Properties().rarity(Rarity.EPIC).tab(Arcana.ARCANAGROUP).stacksTo(1));
        setRegistryName(new ResourceLocation(Arcana.id, "basicwand"));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        f(world, player);
        return super.use(world, player, hand);
    }

    public void f(World world, PlayerEntity player){
        new FireExplosion(world, player, 3.0F,3,4).explode();
    }
}
