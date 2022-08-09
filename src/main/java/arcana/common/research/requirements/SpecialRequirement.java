package arcana.common.research.requirements;

import arcana.common.capability.Knowledge;
import arcana.common.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SpecialRequirement implements Requirement {
    ResourceLocation id;

    public SpecialRequirement() {}

    @Override
    public boolean satisfied(PlayerEntity player) {
        return player.getCapability(Knowledge.CAPABILITY).orElse(null).specialRequirementsMet.contains(id);
    }

    @Override
    public void take(PlayerEntity player) {
    }

    @Override
    public void deserialize(String data) {
        id = new ResourceLocation(data);
    }
}
