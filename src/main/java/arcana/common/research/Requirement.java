package arcana.common.research;

import arcana.common.research.requirements.ItemRequirement;
import arcana.common.research.requirements.SpecialRequirement;
import arcana.common.research.requirements.TagRequirement;
import arcana.common.research.requirements.XpRequirement;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface Requirement {
    boolean satisfied(PlayerEntity player);
    void take(PlayerEntity player);
    void deserialize(String data);

    public static Map<String, Supplier<Requirement>> types = new HashMap<>();
    public static void initParser(){
        types.put("item", ItemRequirement::new);
        types.put("item_tag", TagRequirement::new);
        types.put("special", SpecialRequirement::new);
        types.put("xp", XpRequirement::new);
    }

    public static List<Requirement> parse(JsonObject json){
        List<Requirement> reqs = new ArrayList<>();
        for (JsonElement req : json.get("content").getAsJsonArray()){
            reqs.add(Requirement.parse(req.getAsString()));
        }
        return reqs;
    }

    public static Requirement parse(String string){
        String[] parts = string.split(" ");
        Requirement r = types.get(parts[0]).get();
        r.deserialize(parts[1]);
        return r;
    }
}
