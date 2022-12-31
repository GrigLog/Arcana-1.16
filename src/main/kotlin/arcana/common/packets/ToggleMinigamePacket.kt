package arcana.common.packets

import arcana.common.containers.ResearchTableContainer
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class ToggleMinigamePacket(var seed: Long = 0) : PacketHandler<ToggleMinigamePacket>() {
    override fun decode(buf: PacketBuffer): ToggleMinigamePacket {
        return ToggleMinigamePacket(buf.readLong())
    }

    override fun encode(packet: ToggleMinigamePacket, buf: PacketBuffer) {
        buf.writeLong(packet.seed)
    }

    override fun innerHandle(packet: ToggleMinigamePacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.get().sender
            if (player!!.containerMenu is ResearchTableContainer) {
                (player.containerMenu as ResearchTableContainer).tile!!.minigame.toggle(packet.seed, player)
            }
        }
    }
}