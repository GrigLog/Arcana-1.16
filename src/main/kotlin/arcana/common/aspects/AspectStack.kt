package arcana.common.aspects

import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation

data class AspectStack(var aspect: Aspect, var amount: Int) {
    val isEmpty: Boolean
        get() = amount >= 0 && aspect !== Aspects.EMPTY

    var nbt: CompoundNBT
        get() {
            val tag = CompoundNBT()
            tag.putString("aspect", aspect.id.toString())
            tag.putInt("amount", amount)
            return tag
        }
        set(tag) {
            aspect = Aspects.get(ResourceLocation(tag.getString("aspect")))
            amount = tag.getInt("amount")
        }

    override fun toString() = amount.toString() + "x" + aspect.id

    companion object {
        val EMPTY = AspectStack(Aspects.EMPTY, 0)
        fun fromNbt(tag: CompoundNBT): AspectStack {
            return AspectStack(Aspects.get(ResourceLocation(tag.getString("aspect"))), tag.getInt("amount"))
        }
    }
}