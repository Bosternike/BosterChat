package net.boster.chat.common.provider;

import com.google.common.collect.ImmutableList;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.commands.BosterChatCommand;
import net.boster.chat.common.commands.ChatColorCommand;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.data.EnumStorage;
import net.boster.chat.common.data.ConnectedDatabase;
import net.boster.chat.common.data.database.MySqlConnectionUtils;
import net.boster.chat.common.data.database.SQLiteConnectionUtils;
import net.boster.chat.common.data.setter.DataSetter;
import net.boster.chat.common.data.setter.FileSetter;
import net.boster.chat.common.data.setter.MySqlSetter;
import net.boster.chat.common.data.setter.SQLiteSetter;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.handler.ChatHandlerWrapper;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.utils.ConfigUtils;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.CooldownUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class BosterChatProvider {

    private static BosterChatPlugin plugin;
    private static DataSetter setter;
    private static ConnectedDatabase connectedDatabase;

    public static void setProvider(@NotNull BosterChatPlugin plugin) {
        if(BosterChatProvider.plugin != null) {
            throw new IllegalStateException("Provider already set");
        }

        BosterChatProvider.plugin = plugin;
    }

    public static BosterChatPlugin getProvider() {
        if(plugin == null) {
            throw new IllegalStateException("No provider has been set yet.");
        }

        return plugin;
    }

    public static DataSetter getDataSetter() {
        if(setter == null) {
            throw new IllegalStateException("Plugin hasn't been initialized yet.");
        }

        return setter;
    }

    public static void enable() {
        checkConfig();
        ChatColorProvider.load();
        loadDataSetter();
        loadCooldowns();
        loadChats();

        plugin.registerCommand(new BosterChatCommand(plugin));
        getCommand(plugin.config().getSection("ChatColor"), ChatColorCommand.class);

        ChatHandler.registerHandler(new ChatHandlerWrapper());
    }

    public static void disable() {
        if(connectedDatabase != null) {
            connectedDatabase.closeConnection();
        }
        if (setter != null) {
            setter.onDisable();
        }
    }

    public static void loadCooldowns() {
        CooldownUtils.load(plugin.config().getSection("CooldownFormat"), plugin.config().getSection("CooldownMessage"));
    }

    public static void checkConfig() {
        ConfigurationSection cfg = plugin.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml")));
        File cf = new File(plugin.getDataFolder(), "config.yml");
        if(!ConfigUtils.hasAllStrings(cfg, plugin.loadConfiguration(cf), ImmutableList.of("ChatColor.limitedPermissions"))) {
            ConfigUtils.replaceOldConfig(cf, cf, plugin.getResource("config.yml"));
            plugin.getConfigFile().load();
        }
    }

    public static void loadChats() {
        Chat.clearAll();
        Chat.chats().clear();

        for(String s : plugin.chats().getKeys()) {
            new Chat(plugin.chats().getSection(s), s);
        }

        Chat.sort();
    }

    private static EnumStorage loadStorage() {
        try {
            return EnumStorage.valueOf(BosterChat.get().config().getString("Storage"));
        } catch (Exception e) {
            return EnumStorage.YAML;
        }
    }

    public static void loadDataSetter() {
        if(connectedDatabase != null) {
            connectedDatabase.closeConnection();
        }

        setter = loadDataSetter(loadStorage());
        plugin.log("&7Using storage: &9" + setter.getName(), LogType.INFO);
    }

    private static DataSetter loadDataSetter(EnumStorage storage) {
        if(storage == EnumStorage.MYSQL) {
            String host = plugin.config().getString("MySql.host", "");
            int port = plugin.config().getInt("MySql.port", 3306);
            String user = plugin.config().getString("MySql.user", "");
            String password = plugin.config().getString("MySql.password", "");
            String db = plugin.config().getString("MySql.database", "");

            MySqlConnectionUtils con = new MySqlConnectionUtils(host, port, db, user, password);
            if (con.connect()) {
                connectedDatabase = con;
                return new MySqlSetter(con);
            }
        } else if(storage == EnumStorage.SQLITE) {
            SQLiteConnectionUtils con = new SQLiteConnectionUtils();
            if(con.connect()) {
                connectedDatabase = con;
                return new SQLiteSetter(con);
            }
        }

        return new FileSetter();
    }

    private static <T extends ChatCommandWrapper> void getCommand(@NotNull ConfigurationSection section, @NotNull Class<T> clazz) {
        if(!section.getBoolean("Enabled", false)) return;
        if(section.getString("name") == null) return;

        try {
            plugin.registerCommand(clazz.getDeclaredConstructor(BosterChatPlugin.class, String.class, String[].class)
                    .newInstance(plugin, section.getString("name"), section.getStringList("aliases").toArray(new String[]{})));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
