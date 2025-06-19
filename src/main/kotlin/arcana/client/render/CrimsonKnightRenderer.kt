package arcana.client.render

import arcana.common.entities.CrimsonKnight
import net.minecraft.client.renderer.entity.BipedRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.model.PlayerModel
import net.minecraft.util.ResourceLocation

class CrimsonKnightRenderer(manager: EntityRendererManager)
    // the float parameter is showRadius
    : BipedRenderer<CrimsonKnight, PlayerModel<CrimsonKnight>>(manager, PlayerModel(0.0f, true), 0.5f) {
    override fun getTextureLocation(pEntity: CrimsonKnight): ResourceLocation {
        return super.getTextureLocation(pEntity) //returns steve.png
    }
}