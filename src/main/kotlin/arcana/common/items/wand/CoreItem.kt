package arcana.common.items.wand

import arcana.common.items.ArcanaGroup
import arcana.utils.Util.withPath
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.Color
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import javax.annotation.Nonnull

class CoreItem(val id: ResourceLocation, val maxVis: Int, val difficulty: Int, val level: Int) : Item(ArcanaGroup.props) {
    init {
        registryName = id
        CORES.put(id, this)
    }

    companion object {
        var CORES: MutableMap<ResourceLocation, CoreItem> = LinkedHashMap()
    }

    @OnlyIn(Dist.CLIENT)
    override fun appendHoverText(
        @Nonnull stack: ItemStack,
        world: World?,
        @Nonnull tooltip: MutableList<ITextComponent>,
        @Nonnull flag: ITooltipFlag
    ) {
        super.appendHoverText(stack, world, tooltip, flag)
        tooltip.add(
            StringTextComponent(String.format("MaxVis: %s", maxVis)).setStyle(
                Style.EMPTY.withColor(
                    Color.fromRgb(0xdec7fc)
                )
            )
        )
        tooltip.add(
            StringTextComponent(String.format("Level: %s", level)).setStyle(
                Style.EMPTY.withColor(
                    Color.fromRgb(0xdec7fc)
                )
            )
        )
        tooltip.add(
            StringTextComponent(String.format("Difficulty: %s", difficulty)).setStyle(
                Style.EMPTY.withColor(
                    Color.fromRgb(0xdec7fc)
                )
            )
        )
    }

    fun capAllowed(cap: CapItem): Boolean {
        return level >= cap.level
    }

    val textureLocation: ResourceLocation
        get() = registryName!!.withPath{"models/wands/materials/$it"}
    val guiTexture: ResourceLocation
        get() = registryName!!.withPath{"textures/gui/hud/wand/core/$it"}
}