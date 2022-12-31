package arcana.common.packets

import arcana.common.research.ResearchLoader
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTDynamicOps
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class DataSyncPacket(var files: Map<ResourceLocation, JsonElement>? = null)
    : PacketHandler<DataSyncPacket>() {

    override fun decode(buf: PacketBuffer): DataSyncPacket {
        val n = buf.readVarInt()
        val res: MutableMap<ResourceLocation, JsonElement> = HashMap()
        for (i in 0 until n) {
            res[buf.readResourceLocation()] =
                NBTDynamicOps.INSTANCE.convertTo(JsonOps.INSTANCE, buf.readNbt()!!)
        }
        return DataSyncPacket(res)
    }

    override fun encode(packet: DataSyncPacket, buf: PacketBuffer) {
        buf.writeVarInt(packet.files!!.size)
        packet.files!!.forEach { (rl: ResourceLocation?, json: JsonElement?) ->
            buf.writeResourceLocation(rl)
            buf.writeNbt(JsonOps.INSTANCE.convertTo(NBTDynamicOps.INSTANCE, json) as CompoundNBT)
        }
    }

    override fun innerHandle(packet: DataSyncPacket, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            ResearchLoader.applyClient(packet.files!!)
        }
    }
}