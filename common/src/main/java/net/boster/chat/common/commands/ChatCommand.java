package net.boster.chat.common.commands;

import net.boster.chat.common.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface ChatCommand {

    @NotNull String getName();
    @NotNull String[] getAliases();

    void execute(@NotNull CommandSender sender, @NotNull String[] args);
}
