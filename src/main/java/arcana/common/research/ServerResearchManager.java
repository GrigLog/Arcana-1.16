package arcana.common.research;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerResearchManager {
    public static Map<ResourceLocation, List<Requirement>> requirements = new HashMap<>();
    //TODO: more economic way of synchronization?
    public static Map<ResourceLocation, JsonElement> files;
}
