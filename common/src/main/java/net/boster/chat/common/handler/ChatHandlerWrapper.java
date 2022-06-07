package net.boster.chat.common.handler;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.events.ChatEvent;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;

public class ChatHandlerWrapper extends ChatHandler {

    @Override
    public boolean ignoreCancelled() {
        return false;
    }

    @Override
    public boolean ignoreContinued() {
        return false;
    }

    @Override
    public void onEvent(@NotNull ChatEvent e) {
        Chat c = e.getChat();
        PlayerSender sender = e.getSender();
        if(!c.checkPermission(sender)) {
            e.setContinued(true);
            return;
        }

        e.setCancelled(true);
        if(!c.checkCooldown(sender)) return;

        String message = c.removeStartSymbol(e.getMessage());
        if(message == null) {
            return;
        }

        message = c.checkMessage(sender, message);
        if(message == null) {
            return;
        }

        c.applyCooldown(sender);
        sender.getRecentMessages().add(TextUtils.stripMessage(message));

        message = c.toFormat(sender, message);

        sender.sendToChat(c, message);

        if(sender.getRecentMessages().size() > 5) {
            sender.getRecentMessages().remove(0);
        }
    }
}
