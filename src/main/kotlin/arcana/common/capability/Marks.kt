package arcana.common.capability

import arcana.Arcana
import arcana.common.aspects.Aspects
import arcana.common.packets.MarksPacket
import arcana.common.packets.PacketSender
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.LongNBT
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.network.PacketDistributor
import javax.annotation.Nonnull
import kotlin.math.abs

class Marks(var positions: Array<List<BlockPos>>) {
    constructor() : this(Array(Aspects.primal.size) { emptyList() })

    companion object {
        @CapabilityInject(Marks::class)
        lateinit var CAPABILITY: Capability<Marks>
        var id = ResourceLocation(Arcana.id, "marks")

        const val MARKS_RANGE = 12 //12 chunk radius

        fun sendToClient(@Nonnull player: PlayerEntity) {
            val cap = player.level.getCapability(CAPABILITY!!).resolve().orElse(null)
            val pos = player.blockPosition()
            val toSend = Array(Aspects.primal.size) { i->
                cap!!.positions[i].filter {bp ->
                    abs(bp.x - pos.x) < MARKS_RANGE shl 5 &&
                    abs(bp.z - pos.z) < MARKS_RANGE shl 5
                }
            }
            Marks(toSend).sendToClient(player as ServerPlayerEntity)
        }
    }

    var nbt: CompoundNBT
        get() {
            val tag = CompoundNBT()
            val arr = ListNBT()
            for (posList in positions) {
                val list = ListNBT()
                posList.forEach{ bp -> list.add(LongNBT.valueOf(bp.asLong())) }
                arr.add(list)
            }
            tag.put("positions", arr)
            return tag
        }
        set(tag) {
            val arr = tag["positions"] as ListNBT?
            for (i in Aspects.primal.indices) {
                positions[i] = arr!!.getList(i).map { long -> BlockPos.of((long as LongNBT).asLong) }
            }
        }

    fun sendToClient(@Nonnull player: ServerPlayerEntity?) {
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { player }, MarksPacket(this))
    }

    class Provider : ICapabilitySerializable<INBT?> {
        private val instance = LazyOptional.of { Marks() }
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

    class Storage : IStorage<Marks> {
        override fun writeNBT(capability: Capability<Marks>, cap: Marks, side: Direction?): INBT = cap.nbt

        override fun readNBT(capability: Capability<Marks>, cap: Marks, side: Direction?, nbt: INBT) {
            cap.nbt = nbt as CompoundNBT
        }
    }
}
//can it ever be null?
fun World.getMarks() = this.getCapability(Marks.CAPABILITY).resolve().get()
