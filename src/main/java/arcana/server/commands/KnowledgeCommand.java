package arcana.server.commands;

import arcana.common.capability.Knowledge;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.command.impl.KillCommand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collection;

public class KnowledgeCommand {
    public static int run(CommandContext<CommandSource> ctx){
        CommandSource src = ctx.getSource();
        try {
            Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(ctx, "player");
            profiles.forEach(gp -> {
                PlayerEntity p = src.getServer().getPlayerList().getPlayer(gp.getId());
                if (p != null)
                    src.sendSuccess(new StringTextComponent(strKnowledge(p)), true);
            });
            return 0;
        } catch (CommandSyntaxException ex){
            src.sendFailure(new StringTextComponent(ex.toString()));
            return 1;
        }
    }

    public static int runSelf(CommandContext<CommandSource> ctx){
        CommandSource src = ctx.getSource();
        if (src.getEntity() instanceof PlayerEntity){
            PlayerEntity p = (PlayerEntity) src.getEntity();
            src.sendSuccess(new StringTextComponent(strKnowledge(p)), true);
            return 0;
        } else {
            src.sendFailure(new StringTextComponent("You are not a player!"));
            return 1;
        }
    }

    private static String strKnowledge(PlayerEntity player){
        Knowledge k = player.getCapability(Knowledge.CAPABILITY).resolve().orElse(null);
        StringBuilder sb = new StringBuilder("Special Knowledge: ");
        k.specialRequirementsMet.forEach(rl -> sb.append(rl.toString()).append(','));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\nResearches: ");
        k.researchProgress.forEach((key, progress) -> sb.append(key.toString()).append('/').append(progress).append(','));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
