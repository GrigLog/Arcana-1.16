package arcana.common.packets

/*
import arcana.common.capability.QuantumInventory
import arcana.common.capability.getQuantumInventory
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class QuantumInventoryPacket(var cap: QuantumInventory? = null)
    : PacketHandler<QuantumInventoryPacket>() {
    override fun decode(buf: PacketBuffer): QuantumInventoryPacket {
        val res = QuantumInventoryPacket()
        res.cap!!.nbt = buf.readNbt()!!
        return res;
    }

    override fun encode(packet: QuantumInventoryPacket, buf: PacketBuffer) {
        buf.writeNbt(packet.cap!!.nbt) //todo: more optimal encoding?
    }

    override fun innerHandle(packet: QuantumInventoryPacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            val player: PlayerEntity? = Minecraft.getInstance().player
            if (player == null || !player.isAlive)
                return
            packet.cap!!.copyTo(player.getQuantumInventory())
        } else if (ctx.get().direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.get().sender
            if (player == null || !player.isAlive)
                return
            packet.cap!!.copyTo(player.getQuantumInventory())
        }
    }
}*/