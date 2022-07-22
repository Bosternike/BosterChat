package net.boster.chat.bungee.commands;

import lombok.Getter;
import net.boster.chat.bungee.BosterChatBungee;
import net.boster.chat.bungee.data.PlayerData;
import net.boster.chat.common.commands.ChatCommand;
import net.boster.chat.common.sender.PlayerSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;

public class CustomCommand extends Command implements TabExecutor {

    @Getter @NotNull private final ChatCommand command;

    public CustomCommand(@NotNull ChatCommand command, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.command = command;
    }

    @Override
    public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            command.execute(PlayerData.get((ProxiedPlayer) sender), args);
        } else {
            command.execute(BosterChatBungee.getInstance().getConsole(), args);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return command.onTabComplete(sender instanceof PlayerSender ? PlayerData.get((ProxiedPlayer) sender) : BosterChatBungee.getInstance().getConsole(), args);
    }
}
