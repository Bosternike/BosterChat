package net.boster.chat.common.log;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ChatLog {

    default void log(@NotNull PlayerSender sender, @NotNull Chat chat, @NotNull String message) {
        log(sender, chat, message, null);
    }

    void log(@NotNull PlayerSender sender, @NotNull Chat chat, @NotNull String message, @Nullable List<ChatMessagePlaceholder> placeholders);
}
