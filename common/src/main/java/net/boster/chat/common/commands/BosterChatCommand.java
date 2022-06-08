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
        if(!checkPermission(sender, "boster.chat.command")) return;

        if(args.length == 0) {
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.noArgs")));
            return;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            if(!checkPermission(sender, "boster.chat.command.reload")) return;

            BosterChat.reload();
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.reload")));
        } else if(args[0].equalsIgnoreCase("help")) {
            if(!checkPermission(sender, "boster.chat.command.help")) return;

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

    private boolean checkPermission(CommandSender sender, String s) {
        if(!sender.hasPermission(s)) {
            sender.sendMessage(ChatUtils.toColor(plugin.config().getString("Messages.noPermission")));
            return false;
        } else {
            return true;
        }
    }
}
