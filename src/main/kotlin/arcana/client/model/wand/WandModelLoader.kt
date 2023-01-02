package arcana.client.model.wand

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import net.minecraft.resources.IResourceManager
import net.minecraftforge.client.model.IModelLoader

class WandModelLoader : IModelLoader<WandModelGeometry> {
    override fun onResourceManagerReload(resourceManager: IResourceManager) {}
    override fun read(
        deserializationContext: JsonDeserializationContext,
        modelContents: JsonObject
    ): WandModelGeometry {
        return WandModelGeometry()
    }
}