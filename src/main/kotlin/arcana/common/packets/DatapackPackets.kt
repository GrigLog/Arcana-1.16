package arcana.common.packets

import arcana.common.reloadable.biome_vis.BiomeVisLoader
import arcana.common.reloadable.research.ResearchLoader
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTDynamicOps
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

abstract class DataSyncPacket<SELF : DataSyncPacket<SELF>>(val files: Map<ResourceLocation, JsonElement> = mapOf())
    : PacketHandler<SELF>() {

    override fun decode(buf: PacketBuffer): SELF {
        val n = buf.readVarInt()
        val res: MutableMap<ResourceLocation, JsonElement> = HashMap()
        for (i in 0 until n) {
            res[buf.readResourceLocation()] =
                NBTDynamicOps.INSTANCE.convertTo(JsonOps.INSTANCE, buf.readNbt()!!)
        }
        return selfFactory(res)
    }

    override fun encode(packet: SELF, buf: PacketBuffer) {
        buf.writeVarInt(packet.files.size)
        packet.files.forEach { (rl: ResourceLocation?, json: JsonElement?) ->
            buf.writeResourceLocation(rl)
            buf.writeNbt(JsonOps.INSTANCE.convertTo(NBTDynamicOps.INSTANCE, json) as CompoundNBT)
        }
    }

    override fun innerHandle(packet: SELF, ctx: Supplier<NetworkEvent.Context>) {
        if (ctx.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            applyClient(packet.files)
        }
    }

    abstract fun applyClient(files: Map<ResourceLocation, JsonElement>)
    abstract fun selfFactory(files: Map<ResourceLocation, JsonElement>): SELF
}


class BiomeVisPacket(files: Map<ResourceLocation, JsonElement> = mapOf()) : DataSyncPacket<BiomeVisPacket>(files) {
    override fun applyClient(files: Map<ResourceLocation, JsonElement>) =
        BiomeVisLoader.applyCommon(files)

    override fun selfFactory(files: Map<ResourceLocation, JsonElement>) = BiomeVisPacket(files)
}


class ResearchPacket(files: Map<ResourceLocation, JsonElement> = mapOf()) : DataSyncPacket<ResearchPacket>(files) {
    override fun applyClient(files: Map<ResourceLocation, JsonElement>) =
        ResearchLoader.applyClient(files)

    override fun selfFactory(files: Map<ResourceLocation, JsonElement>) = ResearchPacket(files)
}