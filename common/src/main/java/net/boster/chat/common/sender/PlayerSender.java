package net.boster.chat.common.sender;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.direct.DirectSession;
import net.boster.chat.common.chat.direct.DirectSettings;
import net.boster.chat.common.chat.message.ChatMessage;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.Cooldowns;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull ChatMessage sendToChat(@NotNull Chat chat, @NotNull String message);

    @NotNull String getRank();
    @NotNull ConfigurationSection getData();
    @Nullable MessagePattern getMessagePattern();
    @NotNull List<String> getDisabledChats();
    void setMessagePattern(@Nullable MessagePattern pattern);
    void setData(@NotNull ConfigurationSection data);
    void saveData();

    @NotNull DirectSession getDirectSession();
    @NotNull DirectSettings getDirectSettings();
}
