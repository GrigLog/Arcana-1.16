package arcana.common.items.wand;

import static arcana.utils.Util.arcLoc;

public class StaffItem extends WandItem {

    @Override
    void init(){
        setRegistryName(arcLoc("staff"));
    }
}
