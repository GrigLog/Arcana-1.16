package arcana.data;

import arcana.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt) {
        Arcana.logger.info("gatherData");
        ExistingFileHelper helper = evt.getExistingFileHelper();
        DataGenerator gen = evt.getGenerator();
        gen.addProvider(new ItemModelProvider(gen, helper));
        gen.addProvider(new BlockstateProvider(gen, helper));
        gen.addProvider(new ItemTagsProvider(gen, new ForgeBlockTagsProvider(gen, helper), helper));
    }
}
