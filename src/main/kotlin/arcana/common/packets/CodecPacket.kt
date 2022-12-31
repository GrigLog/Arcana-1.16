package arcana.common.packets

import arcana.utils.Codecs
import com.mojang.serialization.Codec
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import org.apache.commons.lang3.NotImplementedException
import java.util.function.Supplier

open class CodecPacket<T, SELF : PacketHandler<SELF>> (var obj: T? = null)
    : PacketHandler<SELF>() {
    //var obj: T? = null
    open fun getCodec(): Codec<T> = throw NotImplementedException("codec not set")

    //constructor(obj: T) {
    //    this.obj = obj
    //}

    //constructor() {}

    override fun decode(buf: PacketBuffer): SELF {
        return CodecPacket<T, SELF>(Codecs.decodeNbt(buf.readNbt()!!, getCodec())) as SELF
    }

    override fun encode(packet: SELF, buf: PacketBuffer) {
        val self = packet as CodecPacket<T, SELF>
        buf.writeNbt(Codecs.encodeNbt(self.obj!!, true, self.getCodec()))
    }

    override fun innerHandle(packet: SELF, ctx: Supplier<NetworkEvent.Context>) {
        val obj: T = (packet as CodecPacket<T, SELF>).obj!!
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            serverToClient(obj, ctx.get())
        } else if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            clientToServer(obj, ctx.get())
        }
    }

    open fun serverToClient(obj: T, ctx: NetworkEvent.Context) {}
    open fun clientToServer(obj: T, ctx: NetworkEvent.Context) {}
}