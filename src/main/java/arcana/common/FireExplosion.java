package arcana.common;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.*;

public class FireExplosion {

    public float damage;
    public int radius;
    public int burn_time;
    public ExplosionContext damageCalculator;
    public double x;
    public double y;
    public double z;
    public World world;
    public Entity source;

    public FireExplosion(World world, Entity source, float damage, int radius, int burn_time){
        this.world = world;
        this.source = source;
        this.damage = damage;
        this.radius = radius;
        this.burn_time = burn_time;
        this.damageCalculator = (ExplosionContext)(source == null ? new ExplosionContext() : new EntityExplosionContext(source));
        this.x = source.getX();
        this.y = source.getY()+1;
        this.z = source.getZ();
    }

    public void explode() {
        if (world.isClientSide) {
            world.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
            //for (double d = -1.0; d < 1.0; d+=0.1)
            //for(double i = 0; i < 360;i+=4){
            //    double xs = (i<=90?((90-i)/90):i<=180?-(i-90)/90:i<=270?-((270-i)/90):(i-270)/90);
            //    double zs = (i<=90?i/90:i<=180?((180-i)/90):i<=270?-(i-180)/90:-((360-i)/90));
            //    world.addParticle(ParticleTypes.FLAME, x, y, z, xs*radius/8, d*radius/8, zs*radius/8);
            //}
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
            Set<BlockPos> set = Sets.newHashSet();
            int i = 16;
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    for (int l = 0; l < 16; ++l) {
                        if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                            double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                            double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                            double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                            d0 = d0 / d3;
                            d1 = d1 / d3;
                            d2 = d2 / d3;
                            float f = this.radius * (0.7F + world.random.nextFloat() * 0.6F);
                            double d4 = this.x;
                            double d6 = this.y;
                            double d8 = this.z;
                            for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                BlockPos blockpos = new BlockPos(d4, d6, d8);
                                BlockState blockstate = world.getBlockState(blockpos);
                                FluidState fluidstate = world.getFluidState(blockpos);
                                d4 += d0 * (double) 0.3F;
                                d6 += d1 * (double) 0.3F;
                                d8 += d2 * (double) 0.3F;
                            }
                        }
                    }
                }
            }
            //this.toBlow.addAll(set);
            float f2 = this.radius * 2.0F;
            int k1 = MathHelper.floor(this.x - (double) f2 - 1.0D);
            int l1 = MathHelper.floor(this.x + (double) f2 + 1.0D);
            int i2 = MathHelper.floor(this.y - (double) f2 - 1.0D);
            int i1 = MathHelper.floor(this.y + (double) f2 + 1.0D);
            int j2 = MathHelper.floor(this.z - (double) f2 - 1.0D);
            int j1 = MathHelper.floor(this.z + (double) f2 + 1.0D);
            List<Entity> list = world.getEntities(this.source, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
            //net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, this, list, f2);
            Vector3d vector3d = new Vector3d(this.x, this.y, this.z);
            source.setSecondsOnFire(burn_time);
            for (int k2 = 0; k2 < list.size(); ++k2) {
                Entity entity = list.get(k2);
                if (!entity.ignoreExplosion()) {
                    double d12 = (double) (MathHelper.sqrt(entity.distanceToSqr(vector3d)) / f2);
                    if (d12 <= 1.0D) {
                        double d5 = entity.getX() - this.x;
                        double d7 = (entity instanceof TNTEntity ? entity.getY() : entity.getEyeY()) - this.y;
                        double d9 = entity.getZ() - this.z;
                        double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                        if (d13 != 0.0D) {
                            d5 = d5 / d13;
                            d7 = d7 / d13;
                            d9 = d9 / d13;
                            double d14 = (double) Explosion.getSeenPercent(vector3d, entity);
                            double d10 = (1.0D - d12) * d14;
                            entity.hurt(DamageSource.IN_FIRE, damage);
                            entity.setSecondsOnFire(burn_time);

                            double d11 = d10 * 3;
                            if (entity instanceof LivingEntity) {
                                d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                            }

                            entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                            //if (entity instanceof PlayerEntity) {
                            //    PlayerEntity playerentity = (PlayerEntity)entity;
                            //    if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                            //        this.hitPlayers.put(playerentity, new Vector3d(d5 * d10, d7 * d10, d9 * d10));
                            //    }
                            //}
                        }
                    }
                }
            }
        }
    }
}
