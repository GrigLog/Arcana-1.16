package arcana.common.research.sections

import arcana.common.research.BookSection
import com.google.gson.JsonObject
import net.minecraft.util.text.TranslationTextComponent

class TextSection : BookSection() {
    lateinit var text: TranslationTextComponent
    override fun deserialize(json: JsonObject) {
        text = TranslationTextComponent(json["content"].asString)
    }
}