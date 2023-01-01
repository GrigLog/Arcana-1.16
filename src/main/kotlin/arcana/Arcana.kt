package arcana

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

@Mod(Arcana.id)
object Arcana {
    const val id = "arcana"
    val logger = LogManager.getLogger(id)
    init {
        //must be registered before any events
        LOADING_CONTEXT.registerConfig(ModConfig.Type.COMMON, ArcanaConfig.COMMON_SPEC)
        LOADING_CONTEXT.registerConfig(ModConfig.Type.CLIENT, ArcanaConfig.CLIENT_SPEC)
    }
}