package arcana.client.model.wand

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import mcp.MethodsReturnNonnullByDefault
import net.minecraft.resources.IResourceManager
import net.minecraftforge.client.model.IModelLoader
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
class WandModelLoader : IModelLoader<WandModelGeometry> {
    override fun onResourceManagerReload(resourceManager: IResourceManager) {}
    override fun read(
        deserializationContext: JsonDeserializationContext,
        modelContents: JsonObject
    ): WandModelGeometry {
        return WandModelGeometry(null, null, null, null)
    }
}