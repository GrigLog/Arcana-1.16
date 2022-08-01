package arcana.client.events;

import arcana.common.aspects.AspectList;
import arcana.common.aspects.AspectStack;
import arcana.common.aspects.AspectUtils;
import arcana.common.aspects.ItemAspectRegistry;
import arcana.common.capability.Marks;
import arcana.common.particles.MarkParticle;
import arcana.utils.ClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RenderEvents {
    @SubscribeEvent
    static void marks(RenderWorldLastEvent event) {
        ClientWorld world = Minecraft.getInstance().level;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (world == null || player == null)
            return;
        if (player.tickCount % 15 != 0)
            return;
        Marks cap = world.getCapability(Marks.CAPABILITY).resolve().orElse(null);
        for (int i = 0; i < AspectUtils.primalAspects.length; i++) {
            for (BlockPos pos : cap.positions[i]) {
                for (int j = 0; j < 3; j++)
                    world.addParticle(new MarkParticle.Data(AspectUtils.primalAspects[i]),
                        pos.getX() + world.random.nextFloat(), pos.getY() + 0.3, pos.getZ() + world.random.nextFloat(),
                        0, 0.1, 0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderTooltipPost(@Nonnull RenderTooltipEvent.PostText event) {
        if (!Screen.hasShiftDown())
            return;
        AspectList stacks = ItemAspectRegistry.get(event.getStack());
        if (stacks.list.size() > 0) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0, 0, 500);
            RenderSystem.color3f(1F, 1F, 1F);
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.translatef(0F, 0F, mc.getItemRenderer().blitOffset);

            int x = event.getX();
            int y = 10 * (event.getLines().size() - 3) + 14 + event.getY();
            for (AspectStack as : stacks) {
                ClientUtil.renderAspectStack(event.getMatrixStack(), as.getAspect(), as.amount, x, y);
                x += 20;
            }
            RenderSystem.popMatrix();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void makeTooltip(@Nonnull ItemTooltipEvent event) {
        if (!Screen.hasShiftDown())
            return;
        AspectList stacks = ItemAspectRegistry.get(event.getItemStack());
        if (stacks.list.size() > 0) {
            // amount of spaces that need inserting
            int filler = stacks.list.size() * 5;
            // repeat " " *filler
            StringBuilder sb = new StringBuilder();
            for (int __ = 0; __ < filler; __++) {
                String s = " ";
                sb.append(s);
            }
            String collect = sb.toString();
            event.getToolTip().add(new StringTextComponent(collect));
            event.getToolTip().add(new StringTextComponent(collect));
        }
    }
}
