package arcana.server.events;

import arcana.common.aspects.ItemAspectRegistry;
import net.minecraftforge.event.AddReloadListenerEvent;
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
}
