package net.boster.chat.common.commands;

import net.boster.chat.common.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ChatCommand {

    @NotNull String getName();
    @NotNull String[] getAliases();

    void execute(@NotNull CommandSender sender, @NotNull String[] args);

    @Nullable default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
