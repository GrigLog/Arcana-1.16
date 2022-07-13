package arcana.common.events;

import arcana.common.capability.Marks;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityEvents {
    @SubscribeEvent
    static void chunks(AttachCapabilitiesEvent<World> event){
        event.addCapability(Marks.id, new Marks.Provider());
    }
}
