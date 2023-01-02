package arcana.common.reloadable.research

import arcana.common.research.Requirement
import com.google.gson.JsonElement
import net.minecraft.util.ResourceLocation

object ServerResearchHolder {
    var requirements: MutableMap<ResourceLocation, List<Requirement>> = HashMap()

    //TODO: more economic way of synchronization?
    lateinit var files: Map<ResourceLocation, JsonElement>
}