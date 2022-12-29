package arcana.common.packets;

import arcana.utils.Codecs;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class CodecPacket<T, SELF extends PacketHandler<SELF>> extends PacketHandler<SELF>{
    T obj;

    public Codec<T> getCodec() {
        throw new NotImplementedException("codec not set");
    }

    public CodecPacket(T obj) {
        this.obj = obj;
    }

    public CodecPacket(){}

    @Override
    public SELF decode(PacketBuffer buf) {
        return (SELF) new CodecPacket(Codecs.decodeNbt(buf.readNbt(), getCodec()));
    }

    @Override
    public void encode(SELF packet, PacketBuffer buf) {
        CodecPacket<T, SELF> self = (CodecPacket<T, SELF>) packet;
        buf.writeNbt(Codecs.encodeNbt(self.obj, true, self.getCodec()));
    }

    @Override
    public void innerHandle(SELF packet, Supplier<NetworkEvent.Context> ctx) {
        T obj = ((CodecPacket<T, SELF>) packet).obj;
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            serverToClient(obj, ctx.get());
        } else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER){
            clientToServer(obj, ctx.get());
        }
    }

    public void serverToClient(T obj, NetworkEvent.Context ctx) {}

    public void clientToServer(T obj, NetworkEvent.Context ctx) {}
}
