package net.boster.chat.common.commands.subcommands;

import net.boster.chat.common.commands.SubCommand;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends SubCommand {

    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        plugin.config().getStringList("Messages.help").forEach(s -> sender.sendMessage(ChatUtils.toColorAndPrefix(s)));
    }
}
