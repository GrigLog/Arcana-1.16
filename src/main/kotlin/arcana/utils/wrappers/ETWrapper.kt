package arcana.utils.wrappers

import arcana.Arcana
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.world.World
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity
import java.util.function.BiFunction

class ETWrapper<T : Entity?>(cls: EntityClassification, var regName: String, factory: EntityType.IFactory<T>) {
    var builder: EntityType.Builder<T>

    init {
        builder = EntityType.Builder.of(factory, cls)
    }

    fun get(): EntityType<T> {
        return builder.build("").setRegistryName(Arcana.id, regName) as EntityType<T>
    }

    fun sized(a: Float, b: Float): ETWrapper<T> {
        builder.sized(a, b)
        return this
    }

    fun noSummon(): ETWrapper<T> {
        builder.noSummon()
        return this
    }

    fun noSave(): ETWrapper<T> {
        builder.noSave()
        return this
    }

    fun fireImmune(): ETWrapper<T> {
        builder.fireImmune()
        return this
    }

    fun immuneTo(vararg blocks: Block?): ETWrapper<T> {
        builder.immuneTo(*blocks)
        return this
    }

    fun canSpawnFarFromPlayer(): ETWrapper<T> {
        builder.canSpawnFarFromPlayer()
        return this
    }

    fun clientTrackingRange(pRange: Int): ETWrapper<T> {
        builder.clientTrackingRange(pRange)
        return this
    }

    fun updateInterval(interval: Int): ETWrapper<T> {
        builder.updateInterval(interval)
        return this
    }

    fun setUpdateInterval(interval: Int): ETWrapper<T> {
        builder.setUpdateInterval(interval)
        return this
    }

    fun setTrackingRange(range: Int): ETWrapper<T> {
        builder.setTrackingRange(range)
        return this
    }

    fun setShouldReceiveVelocityUpdates(value: Boolean): ETWrapper<T> {
        builder.setShouldReceiveVelocityUpdates(value)
        return this
    }

    fun setCustomClientFactory(customClientFactory: BiFunction<SpawnEntity?, World?, T>?): ETWrapper<T> {
        builder.setCustomClientFactory(customClientFactory)
        return this
    }
}