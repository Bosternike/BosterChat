package net.boster.chat.common.commands.subcommands;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.commands.SubCommand;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload", "rl");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        BosterChat.reload();
        sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.reload")));
    }
}
