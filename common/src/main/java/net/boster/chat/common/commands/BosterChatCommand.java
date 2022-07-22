package net.boster.chat.common.commands;

import lombok.Getter;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.commands.subcommands.HelpCommand;
import net.boster.chat.common.commands.subcommands.PlayerInfoCommand;
import net.boster.chat.common.commands.subcommands.ReloadCommand;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BosterChatCommand extends ChatCommandWrapper {

    @Getter @NotNull private static final List<SubCommand> subCommands = new ArrayList<>();

    public BosterChatCommand(@NotNull BosterChatPlugin plugin) {
        super(plugin, "bosterchat", "chat");

        subCommands.add(new ReloadCommand());
        subCommands.add(new HelpCommand());
        subCommands.add(new PlayerInfoCommand());
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!checkPermission(sender, "boster.chat.command")) return;

        if (args.length == 0) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.noArgs")));
            return;
        }

        String[] sub = new String[args.length - 1];
        System.arraycopy(args, 1, sub, 0, args.length - 1);

        for(SubCommand cmd : subCommands) {
            if(args[0].equalsIgnoreCase(cmd.getName())) {
                onSubCommand(sender, cmd, sub);
                return;
            }

            for(String arg : cmd.getAliases()) {
                if(arg.equalsIgnoreCase(args[0])) {
                    onSubCommand(sender, cmd, sub);
                    return;
                }
            }
        }

        String syntax = "";
        String a = "";
        for (String s : args) {
            syntax += a + s;
            a = " ";
        }
        sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.invalidSyntax").replace("%syntax%", syntax)));
    }

    private void onSubCommand(@NotNull CommandSender sender, @NotNull SubCommand command, @NotNull String[] args) {
        if(!command.checkPermission(sender)) return;

        command.execute(sender, args);
    }

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args.length < 2) {
            return subCommands.stream().map(SubCommand::getName).collect(Collectors.toList());
        }

        String[] sub = new String[]{};
        System.arraycopy(args, 1, sub, 0, args.length - 2);

        for(SubCommand cmd : subCommands) {
            if(args[0].equalsIgnoreCase(cmd.getName()) && cmd.hasPermission(sender)) return cmd.onTabComplete(sender, sub);

            for(String arg : cmd.getAliases()) {
                if(arg.equalsIgnoreCase(args[0]) && cmd.hasPermission(sender)) {
                    return cmd.onTabComplete(sender, sub);
                }
            }
        }

        return null;
    }
}
