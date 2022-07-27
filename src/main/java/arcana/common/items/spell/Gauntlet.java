package arcana.common.items.spell;

import arcana.Arcana;
import net.minecraft.item.Item;

import static arcana.utils.Util.arcLoc;

public class Gauntlet extends Item {

    public Gauntlet(){
        super(new Properties().tab(Arcana.ARCANAGROUP).stacksTo(1));
        setRegistryName(arcLoc("gauntlet"));
    }
}
