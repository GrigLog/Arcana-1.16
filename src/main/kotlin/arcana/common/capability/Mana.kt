package arcana.common.capability

import arcana.common.aspects.Aspects
import arcana.common.packets.ManaPacket
import arcana.common.packets.PacketSender
import arcana.utils.Util
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraft.util.math.MathHelper
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.network.PacketDistributor
import javax.annotation.Nonnull

class Mana {
    companion object {
        @CapabilityInject(Mana::class)
        lateinit var CAPABILITY: Capability<Mana>
        val id = Util.arcLoc("mana")
    }

    var values = FloatArray(Aspects.PRIMAL.size)
    var max: Float

    constructor() {
        max = 10f
    }

    constructor(values: FloatArray, max: Float) {
        this.values = values
        this.max = max
    }

    fun copyTo(other: Mana) {
        other.values = values
        other.max = max
    }

    var nbt: CompoundNBT
        get() {
            val tag = CompoundNBT()
            tag.putIntArray("values", Util.farrToIarr(values))
            tag.putFloat("max", max)
            return tag
        }
        set(tag) {
            values = Util.iarrToFarr(tag.getIntArray("values"))
            max = tag.getFloat("max")
        }


    fun add(toAdd: FloatArray) {
        for (i in values.indices) {
            values[i] = MathHelper.clamp(values[i] + toAdd[i], 0f, max)
        }
    }

    fun tryConsume(toConsume: FloatArray): Boolean {
        var enough = true
        for (i in values.indices)
            enough = enough and (values[i] > toConsume[i])
        if (!enough)
            return false
        for (i in values.indices)
            values[i] -= toConsume[i]
        return true
    }

    fun sendToClient(@Nonnull player: ServerPlayerEntity?) {
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { player }, ManaPacket(this))
    }

    class Provider : ICapabilitySerializable<INBT?> {
        private val instance = LazyOptional.of { Mana() }
        @Nonnull
        override fun <T> getCapability(@Nonnull cap: Capability<T>, side: Direction?): LazyOptional<T> {
            return if (cap === CAPABILITY) instance.cast() else LazyOptional.empty()
        }

        @Nonnull
        override fun <T> getCapability(@Nonnull cap: Capability<T>): LazyOptional<T> {
            return if (cap === CAPABILITY) instance.cast() else LazyOptional.empty()
        }

        override fun serializeNBT(): INBT? {
            return CAPABILITY.storage.writeNBT(CAPABILITY, instance.resolve().get(), null)
        }

        override fun deserializeNBT(nbt: INBT?) {
            CAPABILITY.storage.readNBT(CAPABILITY, instance.resolve().get(), null, nbt)
        }
    }

    class Storage : IStorage<Mana> {
        override fun writeNBT(capability: Capability<Mana>, mana: Mana, side: Direction?) = mana.nbt

        override fun readNBT(capability: Capability<Mana>, mana: Mana, side: Direction?, nbt: INBT) {
            mana.nbt = nbt as CompoundNBT
        }
    }
}

fun LivingEntity.getMana() =  this.getCapability(Mana.CAPABILITY).resolve().get()