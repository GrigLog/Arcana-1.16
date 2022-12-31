package arcana.common.packets

import arcana.common.capability.Knowledge
import arcana.common.capability.getKnowledge
import com.mojang.serialization.Codec
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.fml.network.NetworkEvent

class KnowledgePacket(obj: Knowledge? = null)
    : CodecPacket<Knowledge, KnowledgePacket>(obj) {
    override fun getCodec(): Codec<Knowledge> = Knowledge.CODEC

    override fun serverToClient(obj: Knowledge, ctx: NetworkEvent.Context) {
        val player: PlayerEntity? = Minecraft.getInstance().player
        if (player == null || !player.isAlive)
            return
        obj.copyTo(player.getKnowledge())
    }
}