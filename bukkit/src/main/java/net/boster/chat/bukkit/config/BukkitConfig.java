package net.boster.chat.bukkit.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.bukkit.files.BosterFile;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class BukkitConfig implements net.boster.chat.common.config.ConfigurationSection {

    @Getter @NotNull private final ConfigurationSection initial;
    @Getter private BosterFile file;

    @Override
    public String getCurrentPath() {
        return initial.getCurrentPath();
    }

    @Override
    public net.boster.chat.common.config.ConfigurationSection getSection(@NotNull String path) {
        ConfigurationSection s = initial.getConfigurationSection(path);
        return s != null ? new BukkitConfig(s, file) : null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object object) {
        initial.set(path, object);
    }

    @Override
    public void save() {
        file.save();
    }

    @Override
    public @NotNull Collection<String> getKeys() {
        return initial.getKeys(false);
    }

    @Override
    public Object get(@NotNull String path) {
        return initial.get(path);
    }

    @Override
    public String getString(@NotNull String path) {
        return initial.getString(path);
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        return initial.getBoolean(path);
    }

    @Override
    public int getInt(@NotNull String path) {
        return initial.getInt(path);
    }

    @Override
    public double getDouble(@NotNull String path) {
        return initial.getDouble(path);
    }

    @Override
    public String getString(@NotNull String path, String def) {
        return initial.getString(path, def);
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        return initial.getBoolean(path, def);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        return initial.getInt(path, def);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        return initial.getDouble(path, def);
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull String path) {
        return initial.getStringList(path);
    }

    @Override
    public @NotNull List<Boolean> getBooleanList(@NotNull String path) {
        return initial.getBooleanList(path);
    }

    @Override
    public @NotNull List<Integer> getIntegerList(@NotNull String path) {
        return initial.getIntegerList(path);
    }

    @Override
    public @NotNull List<Double> getDoubleList(@NotNull String path) {
        return initial.getDoubleList(path);
    }
}
