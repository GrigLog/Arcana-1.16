package arcana.server.events;

import arcana.common.aspects.ItemAspectRegistry;
import arcana.server.commands.TestCommand;
import net.minecraft.command.Commands;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber
public class ServerEvents {
    private static ItemAspectRegistry itemAspects;

    @SubscribeEvent
    static void listenData(AddReloadListenerEvent event){
        event.addListener(itemAspects = new ItemAspectRegistry(event.getDataPackRegistries().getRecipeManager()));
    }

    @SubscribeEvent
    static void started(FMLServerStartedEvent event){
        itemAspects.finish(true);
    }

    @SubscribeEvent
    static void regCommands(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("arcana")
            .then(Commands.literal("test").requires(src -> src.hasPermission(4)).executes(TestCommand::run))
        );
    }
}
