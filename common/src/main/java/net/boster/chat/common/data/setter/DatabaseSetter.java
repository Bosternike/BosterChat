package net.boster.chat.common.data.setter;

import lombok.RequiredArgsConstructor;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.data.ConnectedDatabase;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.utils.ConfigUtils;
import net.boster.chat.common.utils.Utils;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class DatabaseSetter implements DataSetter {

    private final ConnectedDatabase db;

    @Override
    public void setUserData(@NotNull String uuid, @NotNull String w, String o) {
        db.setMySqlUserValue(uuid, w, o);
    }

    @Override
    public String getUserData(@NotNull String uuid, @NotNull String value) {
        return db.getMySqlValue(uuid, value);
    }

    @Override
    public @NotNull ConfigurationSection configuration(@NotNull String uuid) {
        ConfigurationSection c = null;
        String data = getUserData(uuid, "data");

        if(data != null) {
            try {
                c = BosterChat.get().loadConfiguration(Utils.decode(data, String.class));
            } catch (Exception e) {
                BosterChat.get().log("Could not load " + uuid + "'s configuration!", LogType.ERROR);
            }
        }

        return c != null ? c : BosterChat.get().emptyConfiguration();
    }

    @Override
    public void save(@NotNull String uuid, @NotNull ConfigurationSection file) {
        setUserData(uuid, "data", Utils.encode(ConfigUtils.configToString(file)));
    }

    @Override
    public void deleteUser(@NotNull String uuid) {
        db.deleteUser(uuid);
    }
}
