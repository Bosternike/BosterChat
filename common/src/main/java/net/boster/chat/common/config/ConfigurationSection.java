package net.boster.chat.common.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ConfigurationSection {

    String getCurrentPath();
    ConfigurationSection getSection(@NotNull String path);

    void set(@NotNull String path, @Nullable Object object);
    void save();

    @NotNull Collection<String> getKeys();

    Object get(@NotNull String path);
    Map<String, Object> entries();

    String getString(@NotNull String path);
    boolean getBoolean(@NotNull String path);
    int getInt(@NotNull String path);
    double getDouble(@NotNull String path);

    String getString(@NotNull String path, String def);
    boolean getBoolean(@NotNull String path, boolean def);
    int getInt(@NotNull String path, int def);
    double getDouble(@NotNull String path, double def);

    @NotNull List<String> getStringList(@NotNull String path);
    @NotNull List<Boolean> getBooleanList(@NotNull String path);
    @NotNull List<Integer> getIntegerList(@NotNull String path);
    @NotNull List<Double> getDoubleList(@NotNull String path);
}
