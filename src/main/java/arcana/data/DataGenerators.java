package arcana.data;

import arcana.Arcana;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataGenerators {
    public static void gatherData(GatherDataEvent evt) {
        Arcana.logger.warn("gatherData");
        ExistingFileHelper helper = evt.getExistingFileHelper();
        if (evt.includeServer()) {

        }
        if (evt.includeClient()) {
            evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), helper));
        }
    }
}
