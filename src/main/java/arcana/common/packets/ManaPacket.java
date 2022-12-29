package arcana.common.packets;

import arcana.common.aspects.Aspects;
import arcana.common.capability.Mana;
import arcana.utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaPacket extends PacketHandler<ManaPacket> {
    Mana cap;
    public ManaPacket(Mana cap) {
        this.cap = cap;
    }

    public ManaPacket() {}

    @Override
    public ManaPacket decode(PacketBuffer buf) {
        float max = buf.readFloat();
        int[] intValues = new int[Aspects.primal.length];
        for (int i = 0; i < intValues.length; i++) {
            intValues[i] = buf.readInt();
        }
        return new ManaPacket(new Mana(Util.iarrToFarr(intValues), max));
    }

    @Override
    public void encode(ManaPacket p, PacketBuffer buf) {
        buf.writeFloat(p.cap.max);
        for (int i = 0; i < Aspects.primal.length; i++) {
            buf.writeInt(Float.floatToIntBits(p.cap.values[i]));
        }
    }

    @Override
    public void innerHandle(ManaPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null || !player.isAlive())
                return;
            packet.cap.copyTo(Mana.unchecked(player));
        } else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER){
            //not required
        }
    }
}
