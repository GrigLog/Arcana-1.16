package arcana.common.packets

import arcana.common.entities.AspectOrbEntity
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier



class AspectOrbRemovedPacket(val id: Int) {
    constructor(orbEntity: AspectOrbEntity) : this(orbEntity.id)
}

class AspectOrbRemovedHandler : PacketHandler2<AspectOrbRemovedPacket>() {
    override fun encode(packet: AspectOrbRemovedPacket, buf: PacketBuffer) {
        buf.writeInt(packet.id)
    }

    override fun decode(buf: PacketBuffer): AspectOrbRemovedPacket {
        return AspectOrbRemovedPacket(buf.readInt())
    }

    override fun innerHandle(packet: AspectOrbRemovedPacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            val orbEntity = Minecraft.getInstance().level!!.getEntity(packet.id) as? AspectOrbEntity
            //orbEntity?.remove()
        } else if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            throw IllegalArgumentException("AspectOrbRemoved packet is meant to be sent from server to client, not vice versa!")
        }
    }
}