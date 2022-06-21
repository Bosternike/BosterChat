package net.boster.chat.common.log;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

public interface ChatLog {

    void log(@NotNull PlayerSender sender, @NotNull Chat chat, @NotNull String message);
}
