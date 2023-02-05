package arcana.client.render

import arcana.utils.Util.arcLoc
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.entity.Entity

class EmptyEntityRenderer<T : Entity>(manager: EntityRendererManager) : EntityRenderer<T>(manager) {
    companion object {
        val EMPTY_TEXTURE = arcLoc("empty")
    }
    override fun getTextureLocation(pEntity: T) = EMPTY_TEXTURE
}