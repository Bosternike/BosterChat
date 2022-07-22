package net.boster.chat.bukkit.commands;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.boster.chat.bukkit.BosterChatBukkit;
import net.boster.chat.bukkit.data.PlayerData;
import net.boster.chat.common.commands.ChatCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomCommand extends BosterCommand {

    @Getter @NotNull private final ChatCommand command;

    public CustomCommand(@NotNull ChatCommand command, @NotNull BosterChatBukkit plugin, @NotNull String name, @NotNull String... aliases) {
        super(plugin, name, aliases);
        this.command = command;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            command.execute(PlayerData.get((Player) sender), args);
        } else {
            command.execute(plugin.getConsole(), args);
        }
        return true;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        net.boster.chat.common.sender.CommandSender cs = sender instanceof Player ? PlayerData.get((Player) sender) : plugin.getConsole();
        List<String> rl = command.onTabComplete(cs, args);

        if(rl != null) {
            return rl;
        } else {
            if (args.length == 0) {
                return ImmutableList.of();
            }

            String lastWord = args[args.length - 1];

            Player senderPlayer = sender instanceof Player ? (Player) sender : null;

            ArrayList<String> matchedPlayers = new ArrayList<>();
            for (Player player : sender.getServer().getOnlinePlayers()) {
                String name = player.getName();
                if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                    matchedPlayers.add(name);
                }
            }

            matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        }
    }
}
