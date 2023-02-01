package arcana.common.entities

import arcana.utils.wrappers.ETWrapper
import net.minecraft.entity.EntityClassification.MISC

object ModEntities {
    @JvmField val ARCANUM = ETWrapper<ArcanumEntity>(MISC, "arcanum", ::ArcanumEntity)
        .sized(1f, 0.4f).clientTrackingRange(6).updateInterval(20).get()
    @JvmField val CHASING_SKULL = ETWrapper<ChasingSkullEntity>(MISC, "chasing_skull", ::ChasingSkullEntity)
        .sized(0.3125F, 0.3125F).clientTrackingRange(4).get()
}