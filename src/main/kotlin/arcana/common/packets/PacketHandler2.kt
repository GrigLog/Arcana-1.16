package arcana.common.packets

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

abstract class PacketHandler2<P> {
    abstract fun decode(buf: PacketBuffer): P
    abstract fun encode(packet: P, buf: PacketBuffer)
    abstract fun innerHandle(packet: P, ctx: Supplier<NetworkEvent.Context>)
    fun handle(packet: P, ctx: Supplier<NetworkEvent.Context>) {
        ctx.get().enqueueWork { innerHandle(packet, ctx) }
        ctx.get().packetHandled = true
    }
}