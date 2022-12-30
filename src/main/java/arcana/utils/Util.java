package arcana.utils;

import arcana.Arcana;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static ResourceLocation arcLoc(String s){
        return new ResourceLocation(Arcana.id, s);
    }

    public static <T> T fromId(Map<ResourceLocation, T> map, String key) {
        return map.get(new ResourceLocation(key));
    }

    public static ResourceLocation withPath(ResourceLocation rl, String path) {
        return new ResourceLocation(rl.getNamespace(), path + rl.getPath());
    }

    public static int[] farrToIarr(float[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = Float.floatToIntBits(arr[i]);
        }
        return res;
    }

    public static float[] iarrToFarr(int[] arr) {
        float[] res = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = Float.intBitsToFloat(arr[i]);
        }
        return res;
    }

}
