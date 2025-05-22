package arcana.common.capability

/*
import kotlin.collections.EmptyMap.values

class QuantumInventory {
    companion object {
        @CapabilityInject(QuantumChestTile::class)
        lateinit var CAPABILITY: Capability<QuantumInventory>
        val id = Util.arcLoc("mana")
    }

    var inventories = Array(8, {EnderChestInventory()})

    constructor() {
        inventories[Direction.DOWN.ordinal].addItem(ItemStack(ModItems.ARCANUM))
    }

    fun copyTo(other: QuantumInventory) {
        inventories.copyInto(other.inventories)
    }

    var nbt: CompoundNBT
        get() {
            val list = ListNBT()
            for (inv in inventories) {
                list.add(inv.createTag())
            }
            val tag = CompoundNBT()
            tag.put("inventories", list)  //TODO: do I need to specify the tag?
            return tag
        }
        set(tag) {
            val list = tag.get("inventories") as ListNBT
            for (i in 0..inventories.size) {
                inventories[i].fromTag(list[i] as ListNBT)
            }
        }

    fun sendToClient(@Nonnull player: ServerPlayerEntity?) {
        PacketSender.INSTANCE.send(PacketDistributor.PLAYER.with { player }, ManaPacket(this))
    }

    class Provider : ICapabilitySerializable<INBT?> {
        private val instance = LazyOptional.of { QuantumInventory() }
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

fun LivingEntity.getQuantumInventory() =  this.getCapability(QuantumInventory.CAPABILITY).resolve().get()*/