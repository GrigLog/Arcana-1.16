package arcana.common.items

import arcana.common.entities.ArcanumEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.NonNullList
import net.minecraft.world.World

class ArcanumItem : Item(ArcanaGroup.props.stacksTo(1)) {

    override fun fillItemCategory(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (allowdedIn(group)) {
            val nbt = CompoundNBT()
            val stack = ItemStack(this.item, 1)
            nbt.putInt("open", 0)
            stack.tag = nbt
            items.add(stack)
        }
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val tag = player.getItemInHand(hand).orCreateTag
        tag.putBoolean("open", !tag.getBoolean("open"))
        return super.use(world, player, hand)
    }

    override fun hasCustomEntity(stack: ItemStack) = true

    override fun createEntity(world: World, original: Entity, itemstack: ItemStack) =
        ArcanumEntity(original as ItemEntity)
}