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
        INSTANCE.registerMessage(msgId++, MarksPacket.class, MarksPacket::encode, MarksPacket::decode, MarksPacket::handle);
        INSTANCE.registerMessage(msgId++, ToggleMinigamePacket.class, ToggleMinigamePacket::encode, ToggleMinigamePacket::decode, ToggleMinigamePacket::handle);
    }
}
