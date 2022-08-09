package arcana.common.research.sections;

import arcana.common.research.BookSection;
import arcana.common.research.Requirement;
import com.google.gson.JsonObject;

import java.util.List;

public class RequirementSection extends BookSection {
    List<Requirement> requirements;

    @Override
    public void deserialize(JsonObject json) {
        requirements = Requirement.parse(json);
    }
}
