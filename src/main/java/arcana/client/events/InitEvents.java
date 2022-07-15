package arcana.client.events;

import arcana.Arcana;
import arcana.common.particles.Mark;
import arcana.common.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitEvents {
    @SubscribeEvent
    static void addTextures(TextureStitchEvent.Pre event){
        if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)){
            event.addSprite(Mark.spriteLoc); //I think particles should use their own texture loading instead but did not figure out how to make it work
        }
    }

    @SubscribeEvent
    static void regParticles(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ModParticles.markType, new Mark.Factory());
    }
}
