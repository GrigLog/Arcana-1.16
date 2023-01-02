package arcana.client.events

import arcana.ArcanaConfig
import arcana.client.ClientPaths.HUD_CORES
import arcana.client.ClientPaths.HUD_VIS
import arcana.common.aspects.Aspect
import arcana.common.aspects.Aspects
import arcana.common.capability.Mana
import arcana.common.capability.getMana
import arcana.common.items.wand.MagicDevice
import arcana.utils.ClientUtil
import arcana.utils.Util.withPath
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
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
        if (player == null || event.type != RenderGameOverlayEvent.ElementType.HOTBAR)
            return
        if (player.mainHandItem.item is MagicDevice || player.offhandItem.item is MagicDevice) {
            val deviceStack = if (player.mainHandItem.item is MagicDevice) player.mainHandItem else player.offhandItem
            val device = deviceStack.item as MagicDevice
            val texture = device.getCore(deviceStack).id.withPath(HUD_CORES)
            val core = device.getCore(deviceStack)
            val spell = device.getSpell(deviceStack)

            val offX = ArcanaConfig.HUD_MANA_X.get()
            val offY = ArcanaConfig.HUD_MANA_Y.get()
            val scale = ArcanaConfig.HUD_MANA_SCALING.get().toFloat()
            val left = ArcanaConfig.HUD_MANA_LEFT.get()
            val top = ArcanaConfig.HUD_MANA_TOP.get()
            val baseX = (if (left) offX / scale else (event.window.guiScaledWidth - offX) / scale - 49).toInt()
            val baseY = (if (top) offY / scale else (event.window.guiScaledHeight - offY) / scale - 49).toInt()
            RenderSystem.pushMatrix()
            RenderSystem.scalef(scale, scale, 2f)

            Minecraft.getInstance().textureManager.bind(texture)
            ClientUtil.blitFullNoScale(event.matrixStack, baseX, baseY, 49, 49)

            renderVisMeter(event.matrixStack, player.getMana(), baseX, baseY)

            //render spell
            //Minecraft.getInstance().itemRenderer.renderGuiItem(spell, baseX + 1, baseY + 1)

            //if (player.isCrouching)
            //    ClientUiUtil.renderVisDetailInfo(event.matrixStack, aspects)
            RenderSystem.popMatrix()
        }
    }

    fun renderVisMeter(ms: MatrixStack, mana: Mana, x: Int, y: Int) {
        val poolOffset = 2
        val poolSpacing = 6
        val poolFromEdge = 24
        // "2": distance to first vis pool
        // "+= 6": distance between vis pools
        // "24": constant distance to vis pool
        //val vertical = arrayOf(Aspects.AIR, Aspects.CHAOS, Aspects.EARTH)
        //val horizontal = arrayOf(Aspects.FIRE, Aspects.ORDER, Aspects.WATER)
        //val primal = arrayOf(AIR, WATER, EARTH, FIRE, ORDER, CHAOS)
        renderVisFill(ms, true, Aspects.ORDER, mana.values[4], mana.max, x + poolFromEdge, y + poolOffset + poolSpacing * 0)
        renderVisFill(ms, true, Aspects.FIRE, mana.values[3], mana.max, x + poolFromEdge, y + poolOffset + poolSpacing * 1)
        renderVisFill(ms, true, Aspects.WATER, mana.values[1], mana.max, x + poolFromEdge, y + poolOffset + poolSpacing * 2)
        renderVisFill(ms, false, Aspects.CHAOS, mana.values[5], mana.max, x + poolOffset + poolSpacing * 0, y + poolFromEdge)
        renderVisFill(ms, false, Aspects.AIR, mana.values[0], mana.max, x + poolOffset + poolSpacing * 1, y + poolFromEdge)
        renderVisFill(ms, false, Aspects.EARTH, mana.values[2], mana.max, x + poolOffset + poolSpacing * 2, y + poolFromEdge)
    }

    fun renderVisFill(ms: MatrixStack, horizontal: Boolean, aspect: Aspect, vis: Float, visMax: Float, x: Int, y: Int) {
        val meterThickness = 3
        val meterLen = 16
        val renderLen = (vis / visMax * meterLen).toInt()
        if (renderLen > 0) {
            Minecraft.getInstance().getTextureManager().bind(aspect.id.withPath(HUD_VIS))
            if (horizontal)
                ClientUtil.blitNoScale(ms, 16, 16, 0, 0, x, y, renderLen, meterThickness)
            else
                ClientUtil.blitNoScale(ms, 16, 16, 0, 0, x, y, meterThickness, renderLen)
        }
    }

}