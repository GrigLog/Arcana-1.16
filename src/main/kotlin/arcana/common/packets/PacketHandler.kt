package arcana.common.packets

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

abstract class PacketHandler<SELF : PacketHandler<SELF>> {
    abstract fun decode(buf: PacketBuffer): SELF
    abstract fun encode(packet: SELF, buf: PacketBuffer)
    abstract fun innerHandle(packet: SELF, ctx: Supplier<NetworkEvent.Context>)
    fun handle(packet: SELF, ctx: Supplier<NetworkEvent.Context>) {
        ctx.get().enqueueWork { innerHandle(packet, ctx) }
        ctx.get().packetHandled = true
    }
}