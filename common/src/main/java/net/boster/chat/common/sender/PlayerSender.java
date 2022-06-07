package net.boster.chat.common.sender;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.cooldowns.Cooldowns;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface PlayerSender extends CommandSender {

    @NotNull List<String> getRecentMessages();
    void setRecentMessages(@NotNull List<String> messages);

    boolean isOnline();

    UUID getUUID();

    void sendMessage(TextComponent... textComponent);

    void sendTitle(@NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut);
    void sendActionbar(@NotNull String message);

    @NotNull Cooldowns getCooldowns();

    void sendToChat(@NotNull Chat chat, @NotNull String message);
}
