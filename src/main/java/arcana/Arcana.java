package arcana;

import arcana.common.ArcanaGroup;
import arcana.common.recipes.ArcanaRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Arcana.id)
public class Arcana {
    public static final String id = "arcana";
    public static final Logger logger = LogManager.getLogger(id);
    public static ArcanaGroup ARCANAGROUP = new ArcanaGroup();

    public Arcana(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ArcanaRecipes.Serializers.SERIALIZERS.register(bus);
    }

}
