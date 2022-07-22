package net.boster.chat.common.handler;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.direct.DirectMessage;
import net.boster.chat.common.chat.message.ChatMessage;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
import net.boster.chat.common.events.DirectMessageEvent;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SimpleDMHandler implements DirectMessageHandler {

    @Getter private final Chat chat;

    public SimpleDMHandler(@NotNull Chat chat) {
        this.chat = chat;
    }

    @Override
    public void onEvent(@NotNull DirectMessageEvent e) {
        if(!(e.getSender() instanceof  PlayerSender)) {
            new Thread(() -> {
                List<ChatRow> senderMessage = chat.getNamedRows().get("Sender");
                if(senderMessage == null) return;

                List<ChatRow> receiverMessage = chat.getNamedRows().get("Receiver");
                if(receiverMessage == null) return;

                e.getSender().sendTo(chat, e.getMessage(), ImmutableList.of(e.getReceiver()), senderMessage.toArray(new ChatRow[]{}));
            }).start();
            return;
        }

        PlayerSender sender = (PlayerSender) e.getSender();
        if(!chat.checkPermission(sender)) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        if(!chat.checkCooldown(sender)) return;

        new Thread(() -> {
            String message = e.getMessage();

            List<ChatRow> senderMessage = chat.getNamedRows().get("Sender");
            if(senderMessage == null) return;

            List<ChatRow> receiverMessage = chat.getNamedRows().get("Receiver");
            if(receiverMessage == null) return;

            List<ChatRow> consoleMessage = chat.getNamedRows().get("Console");

            message = chat.checkMessage(sender, message);
            if(message == null) {
                return;
            }

            chat.applyCooldown(sender);


            DirectMessage msg = new DirectMessage(sender, e.getReceiver(), TextUtils.stripMessage(message));
            sender.getDirectSession().getSentMessages().add(msg);
            e.getReceiver().getDirectSession().getReceivedMessages().add(msg);
            if(!e.isReply()) {
                e.getReceiver().getDirectSession().getNotRepliedMessages().add(msg);
            }

            message = chat.toFormat(sender, message);
            if(sender.getMessagePattern() != null && chat.getChatSettings().canUseMessagePattern(sender)) {
                message = sender.getMessagePattern().process(message);
            }

            List<ChatMessagePlaceholder> placeholders = ImmutableList.of(new ChatMessagePlaceholder("%receiver%", e.getReceiver().getName()));

            if(chat.getChatSettings().getLogFormat() != null) {
                BosterChat.get().getChatLog().log(sender, chat, ChatUtils.stripColors(message), placeholders);
            }

            sender.sendTo(chat, message, ImmutableList.of(sender), false, placeholders, senderMessage.toArray(new ChatRow[]{}));
            sender.sendTo(chat, message, ImmutableList.of(e.getReceiver()), false, placeholders, receiverMessage.toArray(new ChatRow[]{}));

            if(chat.getChatSettings().isLogToConsole() && consoleMessage != null) {
                for(ChatMessage.ChatMessageRow row : sender.sendTo(chat, message, Collections.emptyList(), false, placeholders, consoleMessage.toArray(new ChatRow[]{})).getBox().getRows()) {
                    BosterChat.get().getConsole().sendMessage(row.getMessage());
                }
            }
        }).start();
    }
}
