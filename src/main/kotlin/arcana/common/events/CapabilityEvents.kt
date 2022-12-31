package arcana.common.events

import arcana.common.capability.Knowledge
import arcana.common.capability.Mana
import arcana.common.capability.Marks
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber
object CapabilityEvents {
    @SubscribeEvent
    fun chunks(event: AttachCapabilitiesEvent<World>) {
        event.addCapability(Marks.id, Marks.Provider())
    }

    @SubscribeEvent
    fun players(event: AttachCapabilitiesEvent<Entity>) {
        if (event.getObject() is PlayerEntity) {
            event.addCapability(Knowledge.id, Knowledge.Provider())
            event.addCapability(Mana.id, Mana.Provider())
        }
    }
}