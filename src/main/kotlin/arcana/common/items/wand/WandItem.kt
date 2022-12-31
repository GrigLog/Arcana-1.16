package arcana.common.items.wand

import arcana.common.items.ArcanaGroup
import arcana.common.items.ModItems
import arcana.common.items.spell.Spell
import arcana.common.items.spell.Spells
import arcana.utils.Util
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.UseAction
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.Color
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import javax.annotation.Nonnull

open class WandItem(props: Properties = ArcanaGroup.props)
    : Item(props.stacksTo(1)) {

    fun withCapAndCore(cap: CapItem, core: CoreItem, `var`: String): ItemStack {
        val nbt = CompoundNBT()
        nbt.putString("cap", cap.registryName.toString())
        nbt.putString("core", core.registryName.toString())
        nbt.putString("variant", `var`)
        val stack = ItemStack(asItem(), 1)
        stack.tag = nbt
        return stack
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val stack = player.getItemInHand(hand)
        getSpell(stack)!!.press(world, player)
        player.startUsingItem(hand)
        return ActionResult.consume(stack)
    }

    override fun releaseUsing(stack: ItemStack, world: World, living: LivingEntity, timeLeft: Int) {
        getSpell(stack)!!.release(world, living, Int.MAX_VALUE - timeLeft)
        super.releaseUsing(stack, world, living, timeLeft)
    }

    override fun getUseDuration(stack: ItemStack): Int {
        return Int.MAX_VALUE
    }

    override fun getUseAnimation(stack: ItemStack): UseAction {
        return UseAction.BOW
    }

    override fun fillItemCategory(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (allowdedIn(group)) {
            // iron/wooden, silver/dair, gold/greatwood.json, thaumium/silverwood, void/arcanium
            items.add(withCapAndCore(ModItems.IRON_CAP, ModItems.WOOD_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.SILVER_CAP, ModItems.DAIR_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.GOLD_CAP, ModItems.GREATWOOD_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.THAUMIUM_CAP, ModItems.SILVERWOOD_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.VOID_CAP, ModItems.ARCANIUM_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.VOID_CAP, ModItems.BLAZE_WAND_CORE, "wand"))
            items.add(withCapAndCore(ModItems.IRON_CAP, ModItems.WOOD_WAND_CORE, "staff"))
            items.add(withCapAndCore(ModItems.SILVER_CAP, ModItems.DAIR_WAND_CORE, "staff"))
            items.add(withCapAndCore(ModItems.GOLD_CAP, ModItems.GREATWOOD_WAND_CORE, "staff"))
            items.add(withCapAndCore(ModItems.THAUMIUM_CAP, ModItems.SILVERWOOD_WAND_CORE, "staff"))
            items.add(withCapAndCore(ModItems.VOID_CAP, ModItems.ARCANIUM_WAND_CORE, "staff"))
            items.add(withCapAndCore(ModItems.VOID_CAP, ModItems.BLAZE_WAND_CORE, "staff"))
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun appendHoverText(
        @Nonnull stack: ItemStack,
        world: World?,
        @Nonnull tooltip: MutableList<ITextComponent>,
        @Nonnull flag: ITooltipFlag
    ) {
        super.appendHoverText(stack, world, tooltip, flag)
        val core = getCore(stack)
        val cap = getCap(stack)
        tooltip.add(
            StringTextComponent(String.format("MaxVis: %s", core.maxVis + cap.visStorage)).setStyle(
                Style.EMPTY.withColor(Color.fromRgb(0xdec7fc))
            )
        )
        tooltip.add(StringTextComponent(String.format("Level: %s", core.level)).setStyle(
            Style.EMPTY.withColor(Color.fromRgb(0xdec7fc))))
        tooltip.add(StringTextComponent(String.format("Difficulty: %s", core.difficulty)).setStyle(
            Style.EMPTY.withColor(Color.fromRgb(0xdec7fc))))
        tooltip.add(StringTextComponent(String.format("Complexity: %s", cap.complexity)).setStyle(
            Style.EMPTY.withColor(Color.fromRgb(0xdec7fc))))
    }

    companion object {
        fun getCore(stack: ItemStack): CoreItem {
            return Util.fromId(CoreItem.CORES, stack.orCreateTag.getString("core"))!!
        }

        fun getCap(stack: ItemStack): CapItem {
            return Util.fromId(CapItem.CAPS, stack.orCreateTag.getString("cap"))!!
        }

        fun getFocus(stack: ItemStack): FocusItem {
            return Util.fromId(FocusItem.FOCI, stack.orCreateTag.getString("focus"))!!
        }

        fun getSpell(stack: ItemStack): Spell? {
            val spell_name = stack.orCreateTag.getString("spell")
            return if (spell_name.isEmpty()) Spells.EMPTY else Spells.REGISTRY[ResourceLocation(spell_name)]
        }
    }
}