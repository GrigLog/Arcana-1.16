package arcana.common.research.sections

import arcana.common.research.BookSection
import arcana.common.research.Requirement
import com.google.gson.JsonObject

class RequirementSection : BookSection() {
    lateinit var requirements: List<Requirement>
    override fun deserialize(json: JsonObject) {
        requirements = Requirement.parse(json)
    }
}