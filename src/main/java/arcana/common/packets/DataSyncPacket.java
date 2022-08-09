package arcana.common.packets;

import arcana.common.research.ResearchLoader;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataSyncPacket extends PacketHandler<DataSyncPacket>{
    Map<ResourceLocation, JsonElement> files;

    public DataSyncPacket(){}

    public DataSyncPacket(Map<ResourceLocation, JsonElement> files) {
        this.files = files;
    }

    @Override
    public DataSyncPacket decode(PacketBuffer buf) {
        int n = buf.readVarInt();
        Map<ResourceLocation, JsonElement> res = new HashMap<>();
        for (int i = 0; i < n; i++){
            res.put(buf.readResourceLocation(), NBTDynamicOps.INSTANCE.convertTo(JsonOps.INSTANCE, buf.readNbt()));
        }
        return new DataSyncPacket(res);
    }

    @Override
    public void encode(DataSyncPacket p, PacketBuffer buf) {
        buf.writeVarInt(p.files.size());
        p.files.forEach((rl, json) -> {
            buf.writeResourceLocation(rl);
            buf.writeNbt((CompoundNBT) JsonOps.INSTANCE.convertTo(NBTDynamicOps.INSTANCE, json));
        });
    }

    @Override
    public void innerHandle(DataSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
            ResearchLoader.applyClient(packet.files);
        }
    }
}
