package arcana.common.entities

import arcana.utils.wrappers.ETWrapper
import net.minecraft.entity.EntityClassification.MISC

object ModEntities {
    @JvmField val ARCANUM = ETWrapper(MISC, "arcanum", ::ArcanumEntity)
        .sized(1f, 0.4f).clientTrackingRange(6).updateInterval(20).get()
    @JvmField val CHASING_SKULL = ETWrapper(MISC, "chasing_skull", ::ChasingSkullEntity)
        .sized(0.3125F, 0.3125F).clientTrackingRange(4).get()
    @JvmField val DAMAGING_SHOVEL = ETWrapper(MISC, "damaging_shovel", ::DamagingShovelEntity)
        .sized(0.5f, 0.5f).get()
}