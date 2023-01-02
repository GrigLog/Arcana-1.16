package arcana.common.items.wand

import arcana.common.items.ArcanaGroup
import arcana.common.items.spell.Spells
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class BasicWand : Item(ArcanaGroup.props.rarity(Rarity.EPIC).stacksTo(1)), MagicDevice {

    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        Spells.FIREMELON.press(world, player)
        return super.use(world, player, hand)
    }

    companion object {
        var DAMAGE = 10.0
        var BURN_TIME = 3.0
    }
}