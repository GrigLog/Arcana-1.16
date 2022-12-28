package arcana.common.items.spell;

import arcana.common.aspects.AspectList;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Spell {
    public float castSpeedMult = 0;
    public void press(World world, Entity caster) {}
    public void release(World world, Entity caster, int ticksPassed) {}
}
