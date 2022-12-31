package arcana.common.packets

import arcana.Arcana
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry

object PacketSender {
    private const val PROTOCOL_VERSION = "1"
    val INSTANCE = NetworkRegistry.newSimpleChannel(
        ResourceLocation(Arcana.id, "main"),
        { PROTOCOL_VERSION },
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals)
    private var msgId = 0

    fun init() {
        regPackets(
            MarksPacket(),
            ToggleMinigamePacket(),
            DataSyncPacket(),
            ManaPacket(),
            KnowledgePacket()
        )
    }

    private fun regPackets(vararg packets: PacketHandler<*>) {
        for (p in packets) {
            regPacket(p)
        }
    }

    private fun <T : PacketHandler<T>> regPacket(packetInstance: PacketHandler<T>) {
        INSTANCE.registerMessage(msgId++, packetInstance.javaClass as Class<T>, packetInstance::encode, packetInstance::decode, packetInstance::handle)
    }
}