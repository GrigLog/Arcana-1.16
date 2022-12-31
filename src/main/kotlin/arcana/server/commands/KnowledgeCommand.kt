package arcana.server.commands

import arcana.common.capability.getKnowledge
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.command.CommandSource
import net.minecraft.command.arguments.GameProfileArgument
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.text.StringTextComponent

object KnowledgeCommand {
    fun run(ctx: CommandContext<CommandSource>): Int {
        val src = ctx.source
        try {
            val profiles = GameProfileArgument.getGameProfiles(ctx, "player")
            for (gp in profiles) {
                val p = src.server.playerList.getPlayer(gp.id)
                if (p != null)
                    src.sendSuccess(StringTextComponent(strKnowledge(p)), true)
            }
            return 0
        } catch (ex: CommandSyntaxException) {
            src.sendFailure(StringTextComponent(ex.toString()))
            return 1
        }
    }

    fun runSelf(ctx: CommandContext<CommandSource>): Int {
        val src = ctx.source
        if (src.entity is PlayerEntity) {
            val p = src.entity as PlayerEntity
            src.sendSuccess(StringTextComponent(strKnowledge(p)), true)
            return 0
        } else {
            src.sendFailure(StringTextComponent("You are not a player!"))
            return 1
        }
    }

    private fun strKnowledge(player: PlayerEntity): String {
        val k = player.getKnowledge() //TODO: NPE when player is dead?
        val sb = StringBuilder("Special Knowledge: ")
        for (rl in k.specialRequirementsMet)
            sb.append(rl.toString()).append(',')
        sb.deleteCharAt(sb.length - 1)
        sb.append("\nResearches: ")
        k.researchProgress.forEach { (key, progress) ->
            sb.append(key.toString()).append('/').append(progress).append(',')
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}