package arcana.common.capability

import arcana.Arcana
import arcana.common.packets.KnowledgePacket
import arcana.common.packets.PacketSender
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.StringNBT
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.network.PacketDistributor
import javax.annotation.Nonnull

data class Knowledge(
    var specialRequirementsMet: MutableSet<ResourceLocation> = hashSetOf(),
    var researchProgress: MutableMap<ResourceLocation, Int> = hashMapOf()) {

    fun copyTo(other: Knowledge) {
        other.specialRequirementsMet = specialRequirementsMet
        other.researchProgress = researchProgress
    }

    var nbt: CompoundNBT
        get() {
            val tag = CompoundNBT()
            val specials = ListNBT()
            for (rl in specialRequirementsMet)
                specials.add(StringNBT.valueOf(rl.toString()))
            tag.put("specials", specials)
            val research = ListNBT()
            researchProgress.forEach { (key, progress) ->
                research.add(StringNBT.valueOf(key.toString() + '/' + progress)) }
            tag.put("research", research)
            return tag
        }
        set(tag) {
            specialRequirementsMet.clear()
            researchProgress.clear()
            for (nbt in tag.getList("specials", Constants.NBT.TAG_STRING)) {
                val s = nbt.asString
                specialRequirementsMet.add(ResourceLocation(s))
            }
            for (nbt in tag.getList("research", Constants.NBT.TAG_STRING)) {
                val s = nbt.asString
                val split = s.lastIndexOf('/')
                researchProgress[ResourceLocation(s.substring(0, split))] = s.substring(split + 1).toInt()
            }
        }

    fun sendToClient(@Nonnull player: ServerPlayerEntity?) {
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { player }, KnowledgePacket(this))
    }

    class Provider : ICapabilitySerializable<INBT?> {
        private val instance = LazyOptional.of { Knowledge() }
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

    class Storage : IStorage<Knowledge> {
        override fun writeNBT(capability: Capability<Knowledge>, cap: Knowledge, side: Direction?): INBT = cap.nbt

        override fun readNBT(capability: Capability<Knowledge>, cap: Knowledge, side: Direction?, nbt: INBT) {
            cap.nbt = nbt as CompoundNBT
        }
    }

    companion object {
        @CapabilityInject(Knowledge::class)
        lateinit var CAPABILITY: Capability<Knowledge>
        var id = ResourceLocation(Arcana.id, "knowledge")
        val CODEC = RecordCodecBuilder.create<Knowledge> { builder ->
            builder.group(
                ResourceLocation.CODEC.listOf().fieldOf("specialRequirementsMet")
                    .forGetter { o -> ArrayList(o.specialRequirementsMet) },
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("researchProgress")
                    .forGetter { o -> o.researchProgress }
            ).apply(builder) { reqs, progr -> Knowledge(reqs.toHashSet(), progr) }
        }
    }
}

fun LivingEntity.getKnowledge() = this.getCapability(Knowledge.CAPABILITY).resolve().get()