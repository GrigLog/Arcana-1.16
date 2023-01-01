package arcana.client.events

import arcana.common.items.wand.MagicDevice
import arcana.utils.Util.withPath
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(Dist.CLIENT)
object Huds {
    @SubscribeEvent
    fun render(event: RenderGameOverlayEvent.Post) {
        val player = Minecraft.getInstance().player
        if (player == null || event.type != RenderGameOverlayEvent.ElementType.ALL)
            return
        if (player.mainHandItem.item is MagicDevice || player.offhandItem.item is MagicDevice) {
            val deviceStack = if (player.mainHandItem.item is MagicDevice) player.mainHandItem else player.offhandItem
            val device = deviceStack.item as MagicDevice
            val texture = device.getCore(deviceStack).id.withPath{"textures/gui/hud/core/$it"}

        }
    }
}