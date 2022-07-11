package arcana;

import arcana.common.ArcanaGroup;
import arcana.common.items.ArcanaItems;
import arcana.data.DataGenerators;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Mod(Arcana.id)
public class Arcana {
    public static final String id = "arcana";
    public static final Logger logger = LogManager.getLogger();
    public static ArcanaGroup ARCANAGROUP = new ArcanaGroup();

    public Arcana(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(DataGenerators::gatherData);
        modEventBus.addGenericListener(Item.class, ArcanaItems::register);
    }

    public static void main(String[] args) {
        //for(double i = 0; i < 360;i+=5){
            //    double xs = i<=90?((90 - i)/90):i<=180?-(i-90) / 90:i<=270?-((270 - i)/90):(i-270) / 90;
            //    double zs = i<=90?i / 90:i<=180?((180 - i)/90):i<=270?-(i-180) / 90:-((360-i)/90);
            //    System.out.println(xs + ":" + zs);
            //}
        for (double a = 0; a < 3.14 * 2; a += 0.1){
            System.out.println(3 * sin(a) + ":"+3 * cos(a));
        }
    }
}
