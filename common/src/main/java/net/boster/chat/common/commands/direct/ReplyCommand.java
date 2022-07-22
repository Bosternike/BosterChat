package net.boster.chat.common.commands.direct;

import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.direct.DirectMessage;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.events.DirectMessageEvent;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReplyCommand extends ChatCommandWrapper {

    public ReplyCommand(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        super(plugin, section);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if(!(sender instanceof PlayerSender)) {
            sender.sendMessage(ChatUtils.toColorAndPrefix("%prefix% &cYou must be a player to perform this command."));
            return;
        }
        if(ChatHandler.getDirectMessageHandler() == null) return;
        if(!checkPermission(sender)) return;

        ConfigurationSection c = plugin.getDirectSettingsFile().getConfig();

        if(args.length == 0) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.replyUsage")));
            return;
        }

        PlayerSender plr = (PlayerSender) sender;
        if(plr.getDirectSession().getNotRepliedMessages().isEmpty()) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.nothingToReply")));
            return;
        }

        List<DirectMessage> list = plr.getDirectSession().getNotRepliedMessages();
        PlayerSender r = null;
        int t = 0;

        for(int i = 0; i < list.size(); i++) {
            DirectMessage msg = list.get(i);
            if(msg.getSender().isOnline()) {
                r = msg.getSender();
                t = i;
                break;
            }
        }

        if(r == null) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(c.getString("Messages.nothingToReply")));
            return;
        }

        list.remove(t);

        String message = "";
        String space = "";
        for(int i = 1; i < args.length; i++) {
            message += space + args[i];
            space = " ";
        }

        ChatHandler.directMessageEvent(new DirectMessageEvent(plr, r, message, true));
    }
}
