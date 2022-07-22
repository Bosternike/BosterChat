package net.boster.chat.common.commands.chatsettings;

import com.google.common.collect.ImmutableList;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatSettingsCommand extends ChatCommandWrapper {

    private final List<String> commands = ImmutableList.of("help", "disable", "enable");

    public ChatSettingsCommand(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        super(plugin, section);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!checkPermission(sender)) return;
        if(!(sender instanceof PlayerSender)) return;

        PlayerSender p = (PlayerSender) sender;

        if(args.length == 0) {
            sendHelp(sender);
            return;
        }

        if(args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
        } else if(args[0].equalsIgnoreCase("disable")) {
            if(args.length < 2) {
                sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.disable.usage")));
                return;
            }

            if(Chat.get(args[1]) == null) {
                sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.noChat").replace("%name%", args[1])));
                return;
            }

            if(p.getDisabledChats().contains(args[1])) {
                sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.disable.already").replace("%name%", args[1])));
                return;
            }

            p.getDisabledChats().add(args[1]);
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.disable.success").replace("%name%", args[1])));
        } else if(args[0].equalsIgnoreCase("enable")) {
            if(args.length < 2) {
                sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.enable.usage")));
                return;
            }

            if(!p.getDisabledChats().contains(args[1])) {
                sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.enable.already").replace("%name%", args[1])));
                return;
            }

            p.getDisabledChats().remove(args[1]);
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.chatSettings.enable.success").replace("%name%", args[1])));
        } else {
            String syntax = "";
            String a = "";
            for (String s : args) {
                syntax += a + s;
                a = " ";
            }
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.invalidSyntax").replace("%syntax%", syntax)));
        }
    }

    private void sendHelp(CommandSender sender) {
        for(String s : plugin.config().getStringList("Messages.chatSettings.help")) {
            sender.sendMessage(ChatUtils.toColor(s));
        }
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!(sender instanceof PlayerSender)) return null;

        PlayerSender p = (PlayerSender) sender;

        if(args.length < 2) {
            return commands;
        }

        if(args[0].equalsIgnoreCase("disable")) {
            return Chat.chats().stream().map(Chat::getName).filter(c -> !((PlayerSender) sender).getDisabledChats().contains(c)).collect(Collectors.toList());
        } else if(args[0].equalsIgnoreCase("enable")) {
            return p.getDisabledChats();
        }

        return Collections.emptyList();
    }
}
