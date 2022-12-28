package arcana.common.items.wand;

import arcana.Arcana;
import arcana.common.items.ModItems;
import arcana.common.items.spell.Spell;
import arcana.common.items.spell.Spells;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static arcana.utils.Util.arcLoc;

public class WandItem extends Item {

    public WandItem(){
        super(new Properties().tab(Arcana.ARCANAGROUP).stacksTo(1));
        init();
    }

    void init(){
        setRegistryName(arcLoc("wand"));
    }

    public ItemStack withCapAndCore(CapItem cap, CoreItem core, String var){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("cap", cap.name);
        nbt.putString("core", core.name);
        nbt.putString("variant", var);
        ItemStack stack = new ItemStack(this.asItem(), 1);
        stack.setTag(nbt);
        return stack;
    }

    public static CoreItem getCore(ItemStack stack){
        String core_name = stack.getOrCreateTag().getString("core");
        return CoreItem.CORES.get(core_name);
    }

    public static CapItem getCap(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("cap");
        return CapItem.CAPS.get(cap_name);
    }

    public static FocusItem getFocus(ItemStack stack){
        String cap_name = stack.getOrCreateTag().getString("focus");
        return FocusItem.FOCI.get(cap_name);
    }

    public static Spell getSpell(ItemStack stack) {
        String spell_name = stack.getOrCreateTag().getString("spell");
        if (spell_name.isEmpty())
            return Spells.EMPTY;
        return Spells.REGISTRY.get(new ResourceLocation(spell_name));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        getSpell(stack).press(world, player);
        player.startUsingItem(hand);
        return ActionResult.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity living, int timeLeft) {
        getSpell(stack).release(world, living, Integer.MAX_VALUE - timeLeft);
        super.releaseUsing(stack, world, living, timeLeft);
    }


    @Override
    public int getUseDuration(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BOW;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(allowdedIn(group)){
            // iron/wooden, silver/dair, gold/greatwood.json, thaumium/silverwood, void/arcanium
            items.add(withCapAndCore(ModItems.IRON_CAP, ModItems.WOOD_WAND_CORE, "wand"));
            items.add(withCapAndCore(ModItems.SILVER_CAP, ModItems.DAIR_WAND_CORE, "wand"));
            items.add(withCapAndCore(ModItems.GOLD_CAP, ModItems.GREATWOOD_WAND_CORE, "wand"));
            items.add(withCapAndCore(ModItems.THAUMIUM_CAP, ModItems.SILVERWOOD_WAND_CORE, "wand"));
            items.add(withCapAndCore(ModItems.VOID_CAP,ModItems.ARCANIUM_WAND_CORE, "wand"));
            items.add(withCapAndCore(ModItems.VOID_CAP,ModItems.BLAZE_WAND_CORE, "wand"));

            items.add(withCapAndCore(ModItems.IRON_CAP, ModItems.WOOD_WAND_CORE, "staff"));
            items.add(withCapAndCore(ModItems.SILVER_CAP, ModItems.DAIR_WAND_CORE, "staff"));
            items.add(withCapAndCore(ModItems.GOLD_CAP, ModItems.GREATWOOD_WAND_CORE, "staff"));
            items.add(withCapAndCore(ModItems.THAUMIUM_CAP, ModItems.SILVERWOOD_WAND_CORE, "staff"));
            items.add(withCapAndCore(ModItems.VOID_CAP,ModItems.ARCANIUM_WAND_CORE, "staff"));
            items.add(withCapAndCore(ModItems.VOID_CAP,ModItems.BLAZE_WAND_CORE, "staff"));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag){
        super.appendHoverText(stack, world, tooltip, flag);
        CoreItem core = getCore(stack);
        CapItem cap = getCap(stack);
        tooltip.add(new StringTextComponent(String.format("MaxVis: %s",core.maxVis+cap.visStorage)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Level: %s",core.level)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Difficulty: %s",core.difficulty)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
        tooltip.add(new StringTextComponent(String.format("Complexity: %s",cap.complexity)).setStyle(Style.EMPTY.withColor((Color.fromRgb(0xdec7fc)))));
    }
}
