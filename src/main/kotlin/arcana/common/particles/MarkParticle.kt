package arcana.common.particles

import arcana.common.aspects.Aspect
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.world.ClientWorld
import net.minecraft.particles.ParticleType
import net.minecraft.util.ColorHelper.PackedColor

class MarkParticle(
    aspect: Aspect,
    pLevel: ClientWorld,
    pX: Double,
    pY: Double,
    pZ: Double,
    pXSpeed: Double,
    pYSpeed: Double,
    pZSpeed: Double
) : SpriteTexturedParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed) {
    override fun getRenderType(): IParticleRenderType {
        return PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH
    }

    init {
        val color = aspect.colors[2]
        rCol = PackedColor.red(color) / 256f
        gCol = PackedColor.green(color) / 256f
        bCol = PackedColor.blue(color) / 256f
        alpha = 0.75f
    }

    class Data(var aspect: Aspect) : ClientOnlyParticle.Data() {
        override fun getType(): ParticleType<*> {
            return ModParticles.MARK_TYPE
        }
    }

    class Factory(var sprite: IAnimatedSprite) : IParticleFactory<Data> {
        override fun createParticle(
            data: Data,
            pLevel: ClientWorld,
            pX: Double,
            pY: Double,
            pZ: Double,
            pXSpeed: Double,
            pYSpeed: Double,
            pZSpeed: Double
        ): Particle? {
            val particle = MarkParticle(data.aspect, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed)
            particle.setSpriteFromAge(sprite)
            return particle
        }
    }

    companion object {
        //prevents particles overlap which results in visual artifacts
        var PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH: IParticleRenderType = object : IParticleRenderType {
            override fun begin(pBuilder: BufferBuilder, pTextureManager: TextureManager) {
                RenderSystem.depthMask(false)
                pTextureManager.bind(AtlasTexture.LOCATION_PARTICLES)
                RenderSystem.enableBlend()
                RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                )
                RenderSystem.alphaFunc(516, 0.003921569f)
                pBuilder.begin(7, DefaultVertexFormats.PARTICLE)
            }

            override fun end(pTesselator: Tessellator) {
                pTesselator.end()
                RenderSystem.depthMask(true)
            }

            override fun toString(): String {
                return "arcana:PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH"
            }
        }
    }
}