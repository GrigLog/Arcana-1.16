package arcana.common.events;

import arcana.common.capability.Knowledge;
import arcana.common.capability.Mana;
import arcana.common.capability.Marks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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

    @SubscribeEvent
    static void players(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(Knowledge.id, new Knowledge.Provider());
            event.addCapability(Mana.id, new Mana.Provider());
        }
    }
}
