package arcana.common.packets;

import arcana.common.aspects.AspectUtils;
import arcana.common.capability.Marks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MarksPacket extends PacketHandler<MarksPacket> {
    Marks cap;
    public MarksPacket(Marks cap){
        this.cap = cap;
    }
    public MarksPacket(){}

    public MarksPacket decode(PacketBuffer buf){
        Marks cap = new Marks(new ArrayList[AspectUtils.primalAspects.length]);
        for (int i = 0; i < AspectUtils.primalAspects.length; i++) {
            long[] arr = buf.readLongArray(null);
            cap.positions[i] = Arrays.stream(arr).mapToObj(BlockPos::of).collect(Collectors.toList());
        }
        return new MarksPacket(cap);
    }

    public void encode(MarksPacket packet, PacketBuffer buf){
        for (int i = 0; i < AspectUtils.primalAspects.length; i++) {
            long[] arr = packet.cap.positions[i].stream().mapToLong(BlockPos::asLong).toArray();
            buf.writeLongArray(arr);
        }
    }

    public void innerHandle(MarksPacket packet, Supplier<NetworkEvent.Context> ctx){
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null)
                return;
            Marks clientCap = world.getCapability(Marks.CAPABILITY).resolve().orElse(null);
            clientCap.setNbt(packet.cap.getNbt());
        } else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER){
            //not required
        }
    }
}
