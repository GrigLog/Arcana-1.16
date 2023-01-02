package arcana.common.items.spell

import arcana.Arcana
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

class WaterSpray : Spell() {
    override fun press(world: World, caster: LivingEntity) {
        Arcana.logger.info("water spray")
    }
}