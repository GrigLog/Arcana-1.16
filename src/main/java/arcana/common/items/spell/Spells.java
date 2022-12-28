package arcana.common.items.spell;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static arcana.utils.Util.arcLoc;

public class Spells {
    public static Map<ResourceLocation, Spell> REGISTRY = new HashMap<>();

    public static final Spell EMPTY = register("name", new Spell());
    public static final Spell FIRE_EXPLOSION = register("fire_explosion", new FireExplosion(3, 3, 4));

    private static Spell register(String name, Spell spell) {
        REGISTRY.put(arcLoc(name), spell);
        return spell;
    }
}
