package arcana.client.events


import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import arcana.common.aspects.ItemAspectRegistry
import arcana.common.capability.getMarks
import arcana.common.particles.MarkParticle
import arcana.utils.ClientUtil
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderTooltipEvent.PostText
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import javax.annotation.Nonnull

@EventBusSubscriber(value = [Dist.CLIENT])
object RenderEvents {
    @SubscribeEvent
    fun marks(event: RenderWorldLastEvent) {
        val world = Minecraft.getInstance().level
        val player = Minecraft.getInstance().player
        if (world == null || player == null)
            return
        if (player.tickCount % 15 != 0)
            return
        val marks = world.getMarks()
        for (i in Aspects.PRIMAL.indices) {
            for (pos in marks.positions[i]) {
                for (j in 0..2)
                    world.addParticle(
                        MarkParticle.Data(Aspects.PRIMAL[i]),
                        pos.x + world.random.nextDouble(), pos.y + 0.3, pos.z + world.random.nextDouble(),
                        0.0, 0.1, 0.0
                )
            }
        }
    }

    private var line = 0
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderTooltipPost(@Nonnull event: PostText) {
        if (!Screen.hasShiftDown()) return
        val stacks: AspectList = ItemAspectRegistry.get(event.stack)
        if (stacks.list.size > 0) {
            RenderSystem.pushMatrix()
            RenderSystem.translatef(0f, 0f, 500f)
            RenderSystem.color3f(1f, 1f, 1f)
            val mc = Minecraft.getInstance()
            RenderSystem.translatef(0f, 0f, mc.itemRenderer.blitOffset)
            var x = event.x
            val y = 10 * line + 2 + event.y
            for (ass in stacks) {
                ClientUtil.renderAspectStack(event.matrixStack, ass.aspect, ass.amount, x, y)
                x += 20
            }
            RenderSystem.popMatrix()
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun makeTooltip(@Nonnull event: ItemTooltipEvent) {
        if (!Screen.hasShiftDown()) return
        val stacks: AspectList = ItemAspectRegistry.get(event.itemStack)
        if (stacks.list.size > 0) {
            line = event.toolTip.size
            // amount of spaces that need inserting
            val filler = stacks.list.size * 5
            // repeat " " *filler
            val sb = StringBuilder()
            for (i in 0 until filler) {
                val s = " "
                sb.append(s)
            }
            val collect = sb.toString()
            event.toolTip.add(StringTextComponent(collect))
            event.toolTip.add(StringTextComponent(collect))
        }
    }
}