package arcana.common.research;

import arcana.common.research.sections.*;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class BookSection {
    public abstract void deserialize(JsonObject json);

    public static Map<String, Supplier<BookSection>> types;
    public static void initParser(){
        types = new HashMap<>();
        types.put("requirements", RequirementSection::new);
        types.put("string", TextSection::new);
        types.put("image", ImageSection::new);
        types.put("crafting", CraftingTableSection::new);
        types.put("arcane_crafting", ArcaneCraftingTableSection::new);
    }

    public static BookSection parse(JsonObject json){
        BookSection s = types.get(json.get("type").getAsString()).get();
        s.deserialize(json);
        return s;
    }
}
