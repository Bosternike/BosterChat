package net.boster.chat.bungee.files;

import lombok.Getter;
import net.boster.chat.bungee.config.BungeeConfig;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class BosterChatFileImpl implements BosterChatFile {

    @Getter @NotNull private final BosterFile initial;
    @Getter @NotNull private ConfigurationSection section;

    public BosterChatFileImpl(@NotNull BosterFile file) {
        this.initial = file;
        this.section = new BungeeConfig(file.getConfiguration());
    }

    @Override
    public @NotNull String getName() {
        return initial.getName();
    }

    @Override
    public @NotNull File getFile() {
        return initial.getFile();
    }

    @Override
    public @NotNull ConfigurationSection getConfig() {
        return section;
    }

    @Override
    public void save() {
        initial.save();
    }

    @Override
    public void load() {
        initial.reload();
        section = new BungeeConfig(initial.getConfiguration(), initial, null);
    }
}
