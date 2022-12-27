package arcana.utils.wrappers;

import arcana.Arcana;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class ETWrapper<T extends Entity> {
    String regName;
    EntityType.Builder<T> builder;

    public ETWrapper(EntityType.IFactory<T> factory, EntityClassification cls, String registryName) {
        regName = registryName;
        builder = EntityType.Builder.of(factory, cls);
    }
    @SuppressWarnings("unchecked")
    public EntityType<T> get(){
        return (EntityType<T>) builder.build("").setRegistryName(Arcana.id, regName);
    }

    public ETWrapper<T> sized(float a, float b){
        builder.sized(a, b);
        return this;
    }

    public ETWrapper<T> noSummon() {
        builder.noSummon();
        return this;
    }

    public ETWrapper<T> noSave() {
        builder.noSave();
        return this;
    }

    public ETWrapper<T> fireImmune() {
        builder.fireImmune();
        return this;
    }

    public ETWrapper<T> immuneTo(Block... blocks) {
        builder.immuneTo(blocks);
        return this;
    }

    public ETWrapper<T> canSpawnFarFromPlayer() {
        builder.canSpawnFarFromPlayer();
        return this;
    }

    public ETWrapper<T> clientTrackingRange(int pRange) {
        builder.clientTrackingRange(pRange);
        return this;
    }

    public ETWrapper<T> updateInterval(int interval) {
        builder.updateInterval(interval);
        return this;
    }

    public ETWrapper<T> setUpdateInterval(int interval) {
        builder.setUpdateInterval(interval);
        return this;
    }

    public ETWrapper<T> setTrackingRange(int range) {
        builder.setTrackingRange(range);
        return this;
    }

    public ETWrapper<T> setShouldReceiveVelocityUpdates(boolean value) {
        builder.setShouldReceiveVelocityUpdates(value);
        return this;
    }

    public ETWrapper<T> setCustomClientFactory(java.util.function.BiFunction<net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity, World, T> customClientFactory) {
        builder.setCustomClientFactory(customClientFactory);
        return this;
    }
}
