package net.boster.chat.common.commands;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class BosterChatCommand extends ChatCommandWrapper {

    public BosterChatCommand(@NotNull BosterChatPlugin plugin) {
        super(plugin, "bosterchat", "chat");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!sender.hasPermission("boster.chat.command")) {
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.noPermission")));
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.noArgs")));
            return;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            BosterChat.reload();
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.reload")));
        } else if(args[0].equalsIgnoreCase("help")) {
            plugin.config().getStringList("Messages.help").forEach(s -> sender.sendMessage(ChatUtils.toColor(s)));
        } else {
            String syntax = "";
            String a = "";
            for(String s : args) {
                syntax += a + s;
                a = " ";
            }
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.invalidSyntax").replace("%syntax%", syntax)));
        }
    }
}
