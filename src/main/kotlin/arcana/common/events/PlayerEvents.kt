package arcana.common.events

import arcana.common.capability.getMana
import arcana.common.reloadable.biome_vis.BiomeVis
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber
object PlayerEvents {
    @SubscribeEvent
    fun tick(event: TickEvent.PlayerTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return
        val mana = event.player.getMana() //TODO: move down
        if (!event.player.isAlive)
            return
        val biome = event.player.level.getBiome(event.player.blockPosition())
        val biomeAura = BiomeVis.getBiomeVis(biome)
        mana.add(biomeAura)
        //Arcana.logger.info(Floats.asList(*mana.values) + (if (event.player.level.isClientSide) "client" else "server"))
    }
}