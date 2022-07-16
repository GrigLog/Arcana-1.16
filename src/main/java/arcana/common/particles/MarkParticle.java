package arcana.common.particles;

import arcana.common.aspects.Aspect;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ColorHelper;

import javax.annotation.Nullable;

public class MarkParticle extends SpriteTexturedParticle {
    protected MarkParticle(Aspect aspect, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        int color = aspect.colors.get(2);
        rCol = ColorHelper.PackedColor.red(color) / 256f;
        gCol = ColorHelper.PackedColor.green(color) / 256f;
        bCol = ColorHelper.PackedColor.blue(color) / 256f;
        alpha = 0.75f;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH;
    }

    //prevents particles overlap which results in visual artifacts
    public static IParticleRenderType PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH = new IParticleRenderType() {
        public void begin(BufferBuilder pBuilder, TextureManager pTextureManager) {
            RenderSystem.depthMask(false);
            pTextureManager.bind(AtlasTexture.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(516, 0.003921569F);
            pBuilder.begin(7, DefaultVertexFormats.PARTICLE);
        }

        public void end(Tessellator pTesselator) {
            pTesselator.end();
            RenderSystem.depthMask(true);
        }

        public String toString() {
            return "arcana:PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH";
        }
    };

    public static class Data extends ClientOnlyParticle.Data{
        Aspect aspect;
        public Data(Aspect aspect){
            this.aspect = aspect;
        }
        @Override
        public ParticleType<?> getType() {
            return ModParticles.markType;
        }
    }

    public static class Factory implements IParticleFactory<Data>{
        IAnimatedSprite sprite;
        public Factory(IAnimatedSprite sprite){
            this.sprite = sprite;
        }
        @Nullable
        @Override
        public Particle createParticle(Data data, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            MarkParticle particle = new MarkParticle(data.aspect, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            particle.setSpriteFromAge(sprite);
            return particle;
        }
    }
}
