package arcana.common.research

import com.google.gson.JsonElement
import net.minecraft.util.ResourceLocation

object ServerResearchManager {
    var requirements: MutableMap<ResourceLocation, List<Requirement>> = HashMap()

    //TODO: more economic way of synchronization?
    lateinit var files: Map<ResourceLocation, JsonElement>
}