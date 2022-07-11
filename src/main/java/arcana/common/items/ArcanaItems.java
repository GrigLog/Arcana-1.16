package arcana.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ArcanaItems {
    public static Item FIREWAND = new BasicWand();
    public static void register(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(FIREWAND);
    }
}
