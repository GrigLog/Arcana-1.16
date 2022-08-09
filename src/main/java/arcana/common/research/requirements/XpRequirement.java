package arcana.common.research.requirements;

import arcana.common.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;

public class XpRequirement implements Requirement {
    int minLevel, takeLevel;
    @Override
    public boolean satisfied(PlayerEntity player) {
        return player.experienceLevel >= minLevel;
    }

    @Override
    public void take(PlayerEntity player) {
        player.giveExperienceLevels(-takeLevel);
    }

    @Override
    public void deserialize(String data) {
        String[] parts = data.split("-");
        minLevel = Integer.parseInt(parts[0]);
        takeLevel = Integer.parseInt(parts[1]);
    }
}
