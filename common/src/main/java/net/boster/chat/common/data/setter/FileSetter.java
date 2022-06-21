package net.boster.chat.common.data.setter;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import net.boster.chat.common.utils.ConfigUtils;
import net.boster.chat.common.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class FileSetter implements DataSetter {

    private final BosterChatFile f;

    public FileSetter() {
        this.f = BosterChat.get().createFile("users", null);
    }

    @Override
    public @NotNull String getName() {
        return "File";
    }

    @Override
    public void setUserData(@NotNull String uuid, @NotNull String w, String o) {
        f.getConfig().set(uuid + "." + w, o);
    }

    @Override
    public String getUserData(@NotNull String uuid, @NotNull String value) {
        return f.getConfig().getString(uuid + "." + value);
    }

    @Override
    public @NotNull ConfigurationSection configuration(@NotNull String uuid) {
        ConfigurationSection c = null;

        String data = getUserData(uuid, "data");
        if(data != null) {
            try {
                c = BosterChat.get().loadConfiguration(Utils.decode(data, String.class));
            } catch (Exception e) {
                e.printStackTrace();
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
        setUserData(uuid, "data", null);
    }

    @Override
    public void onDisable() {
        f.save();
    }
}
