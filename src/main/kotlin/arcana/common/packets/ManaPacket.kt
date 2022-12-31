package arcana.common.packets

import arcana.common.aspects.Aspects
import arcana.common.capability.Mana
import arcana.common.capability.getMana
import arcana.utils.Util
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class ManaPacket(var cap: Mana? = null)
    : PacketHandler<ManaPacket>() {
    override fun decode(buf: PacketBuffer): ManaPacket {
        val max = buf.readFloat()
        val intValues = IntArray(Aspects.primal.size)
        for (i in intValues.indices) {
            intValues[i] = buf.readInt()
        }
        return ManaPacket(Mana(Util.iarrToFarr(intValues), max))
    }

    override fun encode(packet: ManaPacket, buf: PacketBuffer) {
        buf.writeFloat(packet.cap!!.max)
        for (i in Aspects.primal.indices) {
            buf.writeInt(java.lang.Float.floatToIntBits(packet.cap!!.values!![i]))
        }
    }

    override fun innerHandle(packet: ManaPacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player: PlayerEntity? = Minecraft.getInstance().player
            if (player == null || !player.isAlive)
                return
            packet.cap!!.copyTo(player.getMana())
        } else if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            //not required
        }
    }
}