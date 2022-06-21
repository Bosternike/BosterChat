package net.boster.chat.bungee.config;

import lombok.Getter;
import net.boster.chat.bungee.files.BosterFile;
import net.boster.chat.common.config.ConfigurationSection;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BungeeConfig implements ConfigurationSection {

    private static Field mapField;

    static {
        try {
            mapField = Configuration.class.getDeclaredField("self");
            mapField.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @Getter @NotNull private final Configuration initial;
    @Getter BosterFile file;
    private Map<String, Object> map;

    public BungeeConfig(@NotNull Configuration initial) {
        this.initial = initial;
        initMap();
    }

    public BungeeConfig(@NotNull Configuration initial, BosterFile file) {
        this.initial = initial;
        this.file = file;
        initMap();
    }

    public BungeeConfig(@NotNull Configuration initial, BosterFile file, @Nullable String currentPath) {
        this.initial = initial;
        this.file = file;
        this.currentPath = currentPath;
        initMap();
    }

    private void initMap() {
        try {
            map = (Map<String, Object>) mapField.get(initial);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @Getter @Nullable String currentPath;

    @Override
    public ConfigurationSection getSection(@NotNull String path) {
        Configuration c = initial.getSection(path);
        return c != null ? new BungeeConfig(c, file, (currentPath == null ? "" : currentPath + ".") + path) : null;
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
        return initial.getKeys();
    }

    @Override
    public Object get(@NotNull String path) {
        return initial.get(path);
    }

    @Override
    public Map<String, Object> entries() {
        return map;
    }

    @Override
    public String getString(@NotNull String path) {
        String s = initial.getString(path);
        return s != null && s.isEmpty() ? null : s;
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
        return initial.getIntList(path);
    }

    @Override
    public @NotNull List<Double> getDoubleList(@NotNull String path) {
        return initial.getDoubleList(path);
    }
}
