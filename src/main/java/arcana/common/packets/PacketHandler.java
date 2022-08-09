package arcana.common.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketHandler<T extends PacketHandler> {
    public abstract T decode(PacketBuffer buf);
    public abstract void encode(T p, PacketBuffer buf);
    public abstract void innerHandle(T packet, Supplier<NetworkEvent.Context> ctx);

    public void handle(T packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> innerHandle(packet, ctx));
        ctx.get().setPacketHandled(true);
    }
}
