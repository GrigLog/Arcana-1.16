package arcana;

import arcana.common.ArcanaGroup;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Arcana.id)
public class Arcana {
    public static final String id = "arcana";
    public static final Logger logger = LogManager.getLogger(id);
    public static ArcanaGroup ARCANAGROUP = new ArcanaGroup();

    public Arcana(){
    }

}
