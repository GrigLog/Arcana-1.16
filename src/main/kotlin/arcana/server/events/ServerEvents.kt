package arcana.server.events

import arcana.common.aspects.ItemAspectRegistry
import arcana.common.packets.BiomeVisPacket
import arcana.common.packets.PacketSender
import arcana.common.packets.ResearchPacket
import arcana.common.reloadable.biome_vis.BiomeVisLoader
import arcana.common.reloadable.research.ResearchLoader
import arcana.common.reloadable.research.ServerResearchHolder
import arcana.server.commands.KnowledgeCommand
import net.minecraft.command.Commands
import net.minecraft.command.arguments.GameProfileArgument
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.OnDatapackSyncEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.server.FMLServerStartedEvent
import net.minecraftforge.fml.network.PacketDistributor

@EventBusSubscriber
object ServerEvents {
    lateinit var itemAspects: ItemAspectRegistry

    @SubscribeEvent
    fun listenData(event: AddReloadListenerEvent) {
        itemAspects = ItemAspectRegistry(event.dataPackRegistries.recipeManager)
        event.addListener(itemAspects)
        event.addListener(ResearchLoader())
        event.addListener(BiomeVisLoader())
    }

    @SubscribeEvent
    fun started(event: FMLServerStartedEvent) {
        itemAspects.finish(true)
    }

    @SubscribeEvent
    fun regCommands(event: RegisterCommandsEvent) {
        event.dispatcher.register(Commands.literal("arcana").requires { it.hasPermission(4) }
            .then(Commands.literal("knowledge")
                .then(Commands.literal("get")
                    .executes(KnowledgeCommand::runSelf)
                    .then(Commands.argument("player", GameProfileArgument.gameProfile())
                        .executes(KnowledgeCommand::run)
                    )
                )
            )
        )
    }

    @SubscribeEvent
    fun syncData(event: OnDatapackSyncEvent) {
        val dataPackets = listOf(ResearchPacket(ServerResearchHolder.files), BiomeVisPacket(BiomeVisLoader.files))
        if (event.player != null) {
            for (dp in dataPackets)
                PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { event.player }, dp)
        } else {
            for (player in event.playerList.players) {
                for (dp in dataPackets)
                    PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { player }, dp)
            }
        }
    }
}