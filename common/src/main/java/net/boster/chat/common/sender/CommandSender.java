package net.boster.chat.common.sender;

import net.boster.chat.common.button.ButtonBoundActions;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.message.ChatMessage;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface CommandSender {

    String getName();

    void sendMessage(String message);

    default @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> receivers, @NotNull ChatRow... rows) {
        return sendTo(chat, message, receivers, true, null, rows);
    }

    default @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> receivers, @Nullable List<ChatMessagePlaceholder> placeholders, @NotNull ChatRow... rows) {
        return sendTo(chat, message, receivers, true, placeholders, rows);
    }

    default @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> receivers, boolean allowLogToConsole, @NotNull ChatRow... rows) {
        return sendTo(chat, message, receivers, allowLogToConsole, null, rows);
    }

    @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> receivers,
                                boolean allowLogToConsole, @Nullable List<ChatMessagePlaceholder> placeholders, @NotNull ChatRow... rows);

    boolean hasPermission(@NotNull String s);

    @NotNull ButtonBoundActions getButtonBoundActions();
    void setButtonBoundActions(@NotNull ButtonBoundActions actions);
}
