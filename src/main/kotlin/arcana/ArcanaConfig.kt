package arcana

import net.minecraftforge.common.ForgeConfigSpec

object ArcanaConfig {
    private val COMMON_BUILDER = ForgeConfigSpec.Builder()
    private val CLIENT_BUILDER = ForgeConfigSpec.Builder()

    init {
        COMMON_BUILDER.push("Common")
        CLIENT_BUILDER.push("Client")
    }

    val HUD_MANA_SCALING = CLIENT_BUILDER
        .comment("Texture scaling for the wand HUD.", "1.5 by default.")
        .define("WandHudScaling", 1.5)
    val HUD_MANA_X = CLIENT_BUILDER
        .comment("Distance of Wand HUD from the horizontal edge.", "5 by default.")
        .define<kotlin.Double?>("WandHrznOffset", 5.0)
    val HUD_MANA_Y = CLIENT_BUILDER
        .comment("Distance of Wand HUD from the vertical edge.", "5 by default.")
        .define("WandVertOffset", 5.0)
    val HUD_MANA_LEFT = CLIENT_BUILDER
        .comment("Whether the wand HUD should display on the left of the screen (true) or the right (false).", "True by default.")
        .define("WandHudHorizontalSide", true)
    val HUD_MANA_TOP = CLIENT_BUILDER
        .comment("Whether the wand HUD should display on the top of the screen (true) or the bottom (false).", "True by default.")
        .define("WandHudVerticalSide", true)

    val COMMON_SPEC: ForgeConfigSpec = COMMON_BUILDER.build()
    val CLIENT_SPEC: ForgeConfigSpec = CLIENT_BUILDER.build()
}