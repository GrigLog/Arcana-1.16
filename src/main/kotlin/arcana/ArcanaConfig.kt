package arcana

import net.minecraftforge.common.ForgeConfigSpec

object ArcanaConfig {
    private val COMMON_BUILDER = ForgeConfigSpec.Builder()
    private val CLIENT_BUILDER = ForgeConfigSpec.Builder()

    init {
        COMMON_BUILDER.push("Common")
        CLIENT_BUILDER.push("Client")
    }

    val COMMON_SPEC: ForgeConfigSpec = COMMON_BUILDER.build()
    val CLIENT_SPEC: ForgeConfigSpec = CLIENT_BUILDER.build()
}