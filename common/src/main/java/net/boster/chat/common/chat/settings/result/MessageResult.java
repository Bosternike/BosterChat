package net.boster.chat.common.chat.settings.result;

import lombok.Getter;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageResult extends RSResult {

    @Getter private final List<String> message;

    private final boolean checkWords;

    public MessageResult(@NotNull List<String> list, @NotNull List<String> message, boolean checkWords) {
        super(list);
        this.message = message;
        this.checkWords = checkWords;
    }

    private void sendMessage(PlayerSender sender, String content) {
        for(String s : message) {
            sender.sendMessage(ChatUtils.toColor(s.replace("%content%", content)));
        }
    }

    @Override
    public @Nullable String result(@NotNull PlayerSender sender, @NotNull String message) {
        if(checkWords) {
            for(String s : message.split(" ")) {
                for(String l : list) {
                    if(l.equalsIgnoreCase(s)) {
                        sendMessage(sender, l);
                        return null;
                    }
                }
            }
        } else {
            for(String s : list) {
                String msg = message.toLowerCase();
                if(msg.contains(s.toLowerCase())) {
                    sendMessage(sender, s);
                    return null;
                }
            }
        }

        return message;
    }
}
