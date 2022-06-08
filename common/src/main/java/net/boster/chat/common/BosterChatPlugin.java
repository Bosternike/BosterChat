package net.boster.chat.common;

import net.boster.chat.common.chat.placeholders.ChatPlaceholders;
import net.boster.chat.common.chat.settings.Settings;
import net.boster.chat.common.commands.ChatCommand;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

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

    @NotNull ConfigurationSection config();
    @NotNull ConfigurationSection chats();

    void registerCommand(@NotNull ChatCommand command);

    File getDataFolder();
    InputStream getResource(@NotNull String s);
    ConfigurationSection loadConfiguration(@NotNull File file);
    ConfigurationSection loadConfiguration(@NotNull Reader reader);

    @NotNull Collection<? extends PlayerSender> getPlayers();
    PlayerSender getPlayer(@NotNull String name);
    PlayerSender getPlayer(@NotNull UUID id);
    <T> PlayerSender toSender(@NotNull T t);
}
