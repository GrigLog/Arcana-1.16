package arcana.utils;

import arcana.Arcana;
import net.minecraft.util.ResourceLocation;

public class Util {
    public static ResourceLocation arcLoc(String s){
        return new ResourceLocation(Arcana.id, s);
    }
}
