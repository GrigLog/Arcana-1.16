package arcana.common.items.spell

import net.minecraft.entity.Entity
import net.minecraft.world.World

open class Spell {
    open val castSpeedMult = 1f

    open fun press(world: World, caster: Entity) {}
    open fun release(world: World?, caster: Entity?, ticksPassed: Int) {}
}