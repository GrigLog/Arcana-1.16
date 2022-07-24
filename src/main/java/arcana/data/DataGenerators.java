package arcana.data;

import arcana.Arcana;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt) {
        Arcana.logger.info("gatherData");
        ExistingFileHelper helper = evt.getExistingFileHelper();
        if (evt.includeServer()) {

        }
        if (evt.includeClient()) {
            evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), helper));
        }
    }
}
