package arcana.common.entities

import arcana.utils.wrappers.ETWrapper
import net.minecraft.entity.EntityClassification

object ModEntities {
    @JvmField val ARCANUM = ETWrapper<ArcanumEntity>(EntityClassification.MISC, "arcanum", ::ArcanumEntity)
        .sized(1f, 0.4f).clientTrackingRange(6).updateInterval(20).get()
}