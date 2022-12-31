package arcana.common.items

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class ResearchNote(props: Properties, var complete: Boolean) : Item(props) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        return if (!complete) super.use(world, player, hand) else super.use(world, player, hand)
        //give research
    }
}