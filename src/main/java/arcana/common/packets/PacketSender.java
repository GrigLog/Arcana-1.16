package arcana.common.packets;

import arcana.Arcana;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketSender {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Arcana.id, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    private static int msgId = 0;

    public static void init() {
        regPackets(new MarksPacket(), new ToggleMinigamePacket(), new DataSyncPacket());
    }



    private static void regPackets(PacketHandler<?> ... packets){
        for (PacketHandler<?> p : packets){
            regPacket(p);
        }
    }

    private static <T extends PacketHandler<?>> void regPacket(PacketHandler<T> packetInstance){
        INSTANCE.registerMessage(msgId++, (Class<T>) packetInstance.getClass(), packetInstance::encode, packetInstance::decode, packetInstance::handle);
    }
}
