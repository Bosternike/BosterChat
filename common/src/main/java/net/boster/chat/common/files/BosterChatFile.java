package net.boster.chat.common.files;

import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface BosterChatFile {

    @NotNull String getName();
    @NotNull File getFile();
    @NotNull ConfigurationSection getConfig();

    void save();
    void load();
}
