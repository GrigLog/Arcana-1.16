package arcana.utils;

import arcana.Arcana;
import arcana.common.aspects.AspectList;
import arcana.common.aspects.AspectStack;
import arcana.common.aspects.ItemAspectRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
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

    public static ResourceLocation arcLoc(String s){
        return new ResourceLocation(Arcana.id, s);
    }
}
