package arcana.common.packets;

import arcana.common.aspects.AspectUtils;
import arcana.common.capability.Marks;
import arcana.common.containers.ResearchTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ToggleMinigamePacket extends PacketHandler<ToggleMinigamePacket> {
    long seed;
    public ToggleMinigamePacket(){}

    public ToggleMinigamePacket(long seed) {
        this.seed = seed;
    }

    public ToggleMinigamePacket decode(PacketBuffer buf){
        return new ToggleMinigamePacket(buf.readLong());
    }

    public void encode(ToggleMinigamePacket packet, PacketBuffer buf){
        buf.writeLong(packet.seed);
    }

    public void innerHandle(ToggleMinigamePacket packet, Supplier<NetworkEvent.Context> ctx){
         if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER){
            ServerPlayerEntity player = ctx.get().getSender();
            if (player.containerMenu instanceof ResearchTableContainer){
                ((ResearchTableContainer)player.containerMenu).tile.minigame.toggle(packet.seed, player);
            }
        }
    }
}
