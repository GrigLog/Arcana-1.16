package arcana.common.entities;

import arcana.utils.wrappers.ETWrapper;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;


public class ModEntities {

    public static final EntityType<ArcanumEntity> ARCANUM = new ETWrapper<ArcanumEntity>(ArcanumEntity::new, EntityClassification.MISC, "arcanum")
        .sized(1F, 0.4F).clientTrackingRange(6).updateInterval(20).get();
}
