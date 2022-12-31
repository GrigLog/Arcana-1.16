package arcana.common.packets

import arcana.common.aspects.Aspects
import arcana.common.capability.Marks
import arcana.common.capability.getMarks
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class MarksPacket(var cap: Marks? = null)
    : PacketHandler<MarksPacket>() {
    override fun decode(buf: PacketBuffer): MarksPacket {
        val cap = Marks(Array(Aspects.primal.size) {
            val arr = buf.readLongArray(null)
            arr.map{pPackedPos: Long -> BlockPos.of(pPackedPos)}.toMutableList()
        })
        return MarksPacket(cap)
    }

    override fun encode(packet: MarksPacket, buf: PacketBuffer) {
        for (i in Aspects.primal.indices) {
            val arr = packet.cap!!.positions[i].stream().mapToLong { obj: BlockPos? -> obj!!.asLong() }.toArray()
            buf.writeLongArray(arr)
        }
    }

    override fun innerHandle(packet: MarksPacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            val world = Minecraft.getInstance().level ?: return
            val clientCap = world.getMarks()
            clientCap.nbt = packet.cap!!.nbt
        } else if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            //not required
        }
    }
}