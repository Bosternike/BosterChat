package net.boster.chat.common.commands.direct;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.events.DirectMessageEvent;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class DirectMessageCommand extends ChatCommandWrapper {

    public DirectMessageCommand(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        super(plugin, section);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(ChatHandler.getDirectMessageHandler() == null) return;
        if(!checkPermission(sender)) return;

        ConfigurationSection c = plugin.getDirectSettingsFile().getConfig();

        if(args.length < 2) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.msgUsage")));
            return;
        }

        PlayerSender player = plugin.getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.noSuchPlayer").replace("%name%", args[0])));
            return;
        }

        String message = "";
        String space = "";
        for(int i = 1; i < args.length; i++) {
            message += space + args[i];
            space = " ";
        }

        if(!(sender instanceof PlayerSender)) {
            BosterChat.sendFromConsole(message, player);
            return;
        }

        PlayerSender plr = (PlayerSender) sender;
        if(plr == player && c.getBoolean("NoSelf.Enabled", false)) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("NoSelf.message")));
            return;
        }

        if(!player.getDirectSettings().isEnabled()) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.noSuchPlayer").replace("%name%", args[0])));
            return;
        }

        if(player.getDirectSettings().isIgnored(plr.getName())) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.ignoring").replace("%name%", args[0])));
            return;
        }

        ChatHandler.directMessageEvent(new DirectMessageEvent(plr, player, message, false));
    }
}
