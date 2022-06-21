package net.boster.chat.common.data.setter;

import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface DataSetter {

    @NotNull String getName();

    void setUserData(@NotNull String uuid, @NotNull String w, String o);

    String getUserData(@NotNull String uuid, @NotNull String value);

    @NotNull ConfigurationSection configuration(@NotNull String uuid);

    void save(@NotNull String uuid, @NotNull ConfigurationSection file);

    void deleteUser(@NotNull String uuid);

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    default void onDisable() {
    }
}
