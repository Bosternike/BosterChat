package net.boster.chat.common;

import net.boster.chat.common.chat.placeholders.ChatPlaceholders;
import net.boster.chat.common.chat.settings.Settings;
import net.boster.chat.common.commands.ChatCommand;
import net.boster.chat.common.commands.RegisteredCommand;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import net.boster.chat.common.log.ChatLog;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.UUID;

public interface BosterChatPlugin {

    @NotNull CommandSender getConsole();
    void log(@NotNull String s, @NotNull LogType log);

    @NotNull Settings getSettings();
    @NotNull ChatPlaceholders getPlaceholders();

    @NotNull BosterChatFile getConfigFile();
    @NotNull BosterChatFile getChatsFile();
    @NotNull BosterChatFile getDirectSettingsFile();

    @NotNull ConfigurationSection config();
    @NotNull ConfigurationSection chats();

    @NotNull RegisteredCommand registerCommand(@NotNull ChatCommand command);

    File getDataFolder();
    InputStream getResource(@NotNull String s);
    ConfigurationSection loadConfiguration(@NotNull File file);
    ConfigurationSection loadConfiguration(@NotNull Reader reader);
    ConfigurationSection loadConfiguration(@NotNull String s);
    ConfigurationSection emptyConfiguration();

    BosterChatFile createFile(@NotNull String name, @Nullable String directory);

    @NotNull Collection<? extends PlayerSender> getPlayers();
    PlayerSender getPlayer(@NotNull String name);
    PlayerSender getPlayer(@NotNull UUID id);
    <T> PlayerSender toSender(@NotNull T t);

    @NotNull ChatLog getChatLog();
    void setChatLog(@NotNull ChatLog log);

    @NotNull HoverEvent createHover(@NotNull String s);

    void savePlayersData();
    void clearPlayersData();
}
