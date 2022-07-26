package arcana.utils;

import arcana.Arcana;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static <T> List<T> getFields(Class<?> cls, Class<?> fieldClass, @Nullable Object instance){
        List<T> res = new ArrayList<>();
        for (Field f : cls.getDeclaredFields()){
            try {
                Object o = f.get(instance);
                if (fieldClass.isInstance(o))
                    res.add((T) o);
            } catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return res;
    }
}
