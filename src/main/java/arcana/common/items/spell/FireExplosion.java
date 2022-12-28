package arcana.common.items.spell;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.EntityExplosionContext;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

import static java.lang.Math.*;

public class FireExplosion extends Spell {
    public float damage;
    public int radius;
    public int burn_time;

    public FireExplosion(float damage, int radius, int burn_time){ //will be allowed to modify with a config
        this.damage = damage;
        this.radius = radius;
        this.burn_time = burn_time;
    }

    @Override
    public void press(World world, Entity caster) {
        double x = caster.getX(), y = caster.getY() + 1, z = caster.getZ();
        if (world.isClientSide) {
            world.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
            for (double b = -PI; b < PI; b+=0.1) {
                double r = radius * cos(b)/8;
                double ys = radius * sin(b)/8;
                for (double a = 0; a < PI * 2; a += 0.314) {
                    double xs = r*sin(a);
                    double zs = r*cos(a);
                    world.addParticle(ParticleTypes.FLAME, x, y, z, xs, ys, zs);
                }
            }
        } else {
            double diam = this.radius * 2.0F;
            int x1 = MathHelper.floor(x - diam - 1.0D);
            int x2 = MathHelper.floor(x + diam + 1.0D);
            int y1 = MathHelper.floor(y - diam - 1.0D);
            int y2 = MathHelper.floor(y + diam + 1.0D);
            int z1 = MathHelper.floor(z - diam - 1.0D);
            int z2 = MathHelper.floor(z + diam + 1.0D);
            List<Entity> list = world.getEntities(caster, new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
            //net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, this, list, f2);
            Vector3d pos = caster.position();
            caster.setSecondsOnFire(burn_time);
            for (Entity entity : list) {
                if (!entity.ignoreExplosion()) {
                    double dist = (MathHelper.sqrt(entity.distanceToSqr(pos)) / diam);
                    if (dist <= 1.0D) {
                        Vector3d to = new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ()).subtract(pos);
                        double distTo = to.length();
                        if (distTo != 0.0D) {
                            to = to.scale(1 / distTo);
                            double seenPercent = Explosion.getSeenPercent(pos, entity);
                            double d10 = (1.0D - dist) * seenPercent;
                            entity.hurt(DamageSource.IN_FIRE, damage);
                            entity.setSecondsOnFire(burn_time);

                            double knockback = d10 * 3;
                            if (entity instanceof LivingEntity) {
                                knockback = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                            }

                            entity.setDeltaMovement(entity.getDeltaMovement().add(to.scale(knockback)));
                        }
                    }
                }
            }
        }
    }
}
