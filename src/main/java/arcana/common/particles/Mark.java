package arcana.common.particles;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.Aspects;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class Mark extends SpriteTexturedParticle {
    public static final ResourceLocation spriteLoc = new ResourceLocation(Arcana.id, "particle/mark");

    protected  static TextureAtlasSprite SPRITE = null;
    protected Mark(Aspect aspect, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        int color = aspect.colors.get(0);
        rCol = ColorHelper.PackedColor.red(color) / 256f;
        gCol = ColorHelper.PackedColor.green(color) / 256f;
        bCol = ColorHelper.PackedColor.blue(color) / 256f;
        //todo: make it brighter? Add lightning?
        if (SPRITE == null)
            SPRITE = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(spriteLoc);
         sprite = SPRITE;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

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
        @Nullable
        @Override
        public Particle createParticle(Data data, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new Mark(data.aspect, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
