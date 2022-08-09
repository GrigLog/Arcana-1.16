package arcana.common.research.sections;

import arcana.common.research.BookSection;
import com.google.gson.JsonObject;
import net.minecraft.util.text.TranslationTextComponent;

public class TextSection extends BookSection {
    TranslationTextComponent text;
    @Override
    public void deserialize(JsonObject json) {
        text = new TranslationTextComponent(json.get("content").getAsString());
    }
}
