package arcana.server.events

import arcana.common.capability.Marks
import arcana.common.capability.getKnowledge
import arcana.common.capability.getMana
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk
import net.minecraftforge.event.entity.player.PlayerEvent.*
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import java.util.*

@EventBusSubscriber
object PlayerEvents {
    @SubscribeEvent
    fun login(event: PlayerLoggedInEvent) {
        sendCaps(event.player)
    }

    @SubscribeEvent
    fun respawn(event: PlayerRespawnEvent) {
        sendCaps(event.player)
    }

    @SubscribeEvent
    fun changeDim(event: PlayerChangedDimensionEvent) {
        sendCaps(event.player)
    }

    @SubscribeEvent
    fun copyCapsOnRespawn(event: Clone) {
        val old = event.original
        val player = event.player
        old.getMana().copyTo(player.getMana())
        old.getKnowledge().copyTo(player.getKnowledge())
    }

    var savedChunks: MutableMap<UUID, ChunkPos> = HashMap()
    @SubscribeEvent
    fun enterChunk(event: EnteringChunk) {
        if (event.entity !is ServerPlayerEntity) return
        val player = event.entity as ServerPlayerEntity
        val saved = savedChunks.getOrDefault(player.uuid, ChunkPos(0, 0))
        if (Math.abs(saved.x - event.newChunkX) >= Marks.MARKS_RANGE
            || Math.abs(saved.z - event.newChunkZ) >= Marks.MARKS_RANGE) {
            savedChunks[player.uuid] = ChunkPos(event.newChunkX, event.newChunkZ)
            Marks.sendToClient(player)
        }
    }

    fun sendCaps(player1: PlayerEntity) {
        val player = player1 as ServerPlayerEntity
        Marks.sendToClient(player)
        player.getMana().sendToClient(player)
        player.getKnowledge().sendToClient(player)
    }
}