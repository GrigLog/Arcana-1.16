package arcana.server.events;

import arcana.common.aspects.ItemAspectRegistry;
import arcana.common.packets.DataSyncPacket;
import arcana.common.packets.PacketSender;
import arcana.common.research.ServerResearchManager;
import arcana.server.commands.KnowledgeCommand;
import arcana.common.research.ResearchLoader;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class ServerEvents {
    private static ItemAspectRegistry itemAspects;

    @SubscribeEvent
    static void listenData(AddReloadListenerEvent event){
        event.addListener(itemAspects = new ItemAspectRegistry(event.getDataPackRegistries().getRecipeManager()));
        event.addListener(new ResearchLoader());
    }

    @SubscribeEvent
    static void started(FMLServerStartedEvent event){
        itemAspects.finish(true);
    }

    @SubscribeEvent
    static void regCommands(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("arcana").requires(src -> src.hasPermission(4))
            .then(Commands.literal("knowledge")
                .then(Commands.literal("get").executes(KnowledgeCommand::runSelf)
                    .then(Commands.argument("player", GameProfileArgument.gameProfile()).executes(KnowledgeCommand::run))
                )
            )
        );
    }

    @SubscribeEvent
    static void syncData(OnDatapackSyncEvent event){
        ServerPlayerEntity player = event.getPlayer();
        if (player != null){
            PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new DataSyncPacket(ServerResearchManager.files));
            return;
        }
        for (ServerPlayerEntity p : event.getPlayerList().getPlayers()) {
            PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), new DataSyncPacket(ServerResearchManager.files));
        }
    }
}
