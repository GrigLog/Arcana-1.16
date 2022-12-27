package arcana.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ArcanumEntity extends ItemEntity {
    public ArcanumEntity(EntityType<? extends ItemEntity> type, World world) {
        super(type, world);
    }

    public ArcanumEntity(ItemEntity original) {
        this(ModEntities.ARCANUM, original.level);
        setPos(original.position().x, original.position().y, original.position().z);
        setDeltaMovement(original.getDeltaMovement());
        setRot(original.getRotationVector().x, original.getRotationVector().y);
        setPickUpDelay(original.pickupDelay);
        setItem(original.getItem());
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tick() {
        super.tick();
        if (!isAlive())
            return;
        RayTraceResult res = ProjectileHelper.getHitResult(this, e -> true);
        if (res.getType() != RayTraceResult.Type.MISS
            && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, res)
            && res.getType() == RayTraceResult.Type.ENTITY) {
            Entity e = ((EntityRayTraceResult) res).getEntity();
            e.hurt(DamageSource.thrown(this, this), 10);
        }
    }
}
