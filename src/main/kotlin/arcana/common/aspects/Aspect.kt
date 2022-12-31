package arcana.common.aspects

import arcana.Arcana
import net.minecraft.util.ResourceLocation

class Aspect(val id: ResourceLocation, val colors: ColorRange) {
    val translationKey: String = "aspect." + id.namespace + "." + id.path

    override fun toString(): String {
        return "Aspect: $id"
    }

    // only valid for primals
    fun getVisMeterTexture() = ResourceLocation(Arcana.id, "textures/gui/hud/wand/vis/$id.png")

    companion object {
        fun create(name: String, colors: ColorRange): Aspect {
            val aspect = Aspect(ResourceLocation(Arcana.id, name), colors)
            Aspects.ASPECTS[aspect.id] = aspect
            return aspect
        }
    }
}