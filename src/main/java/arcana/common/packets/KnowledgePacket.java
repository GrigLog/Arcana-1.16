package arcana.common.packets;

import arcana.common.capability.Knowledge;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

public class KnowledgePacket extends CodecPacket<Knowledge, KnowledgePacket> {
    public KnowledgePacket(Knowledge obj) {
        super(obj);
    }

    public KnowledgePacket(){}

    @Override
    public Codec<Knowledge> getCodec() {
        return Knowledge.CODEC;
    }

    @Override
    public void serverToClient(Knowledge obj, NetworkEvent.Context ctx) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || !player.isAlive())
            return;
        obj.copyTo(Knowledge.unchecked(player));
    }
}
