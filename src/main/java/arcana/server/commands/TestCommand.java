package arcana.server.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class TestCommand {
    public static int run(CommandContext<CommandSource> ctx){
        ctx.getSource().sendSuccess(new StringTextComponent("test"), true);
        return 0;
    }
}
