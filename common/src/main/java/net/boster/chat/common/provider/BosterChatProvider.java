package net.boster.chat.common.provider;

import com.google.common.collect.ImmutableList;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.commands.BosterChatCommand;
import net.boster.chat.common.commands.button.ChatButtonCommandCompleter;
import net.boster.chat.common.commands.chatcolor.ChatColorCommand;
import net.boster.chat.common.commands.ChatCommandWrapper;
import net.boster.chat.common.commands.RegisteredCommand;
import net.boster.chat.common.commands.chatsettings.ChatSettingsCommand;
import net.boster.chat.common.commands.direct.DirectMessageCommand;
import net.boster.chat.common.commands.direct.IgnoreCommand;
import net.boster.chat.common.commands.direct.ReplyCommand;
import net.boster.chat.common.commands.direct.UnIgnoreCommand;
import net.boster.chat.common.data.EnumStorage;
import net.boster.chat.common.data.ConnectedDatabase;
import net.boster.chat.common.data.database.MySqlConnectionUtils;
import net.boster.chat.common.data.database.SQLiteConnectionUtils;
import net.boster.chat.common.data.setter.DataSetter;
import net.boster.chat.common.data.setter.FileSetter;
import net.boster.chat.common.data.setter.MySqlSetter;
import net.boster.chat.common.data.setter.SQLiteSetter;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.handler.SimpleChatHandler;
import net.boster.chat.common.handler.SimpleDMHandler;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.utils.ConfigUtils;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.CooldownUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;

public class BosterChatProvider {

    private static BosterChatPlugin plugin;
    private static DataSetter setter;
    private static ConnectedDatabase connectedDatabase;

    public static RegisteredCommand buttonCmdCompleter;
    public static RegisteredCommand bosterChatCommand;
    public static RegisteredCommand chatColorCommand;
    public static RegisteredCommand chatSettingsCommand;

    private static RegisteredCommand directMessageCommand;
    private static RegisteredCommand replyCommand;
    private static RegisteredCommand ignoreCommand;
    private static RegisteredCommand unIgnoreCommand;

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
        loadDirectSettings();
        loadChats();

        buttonCmdCompleter = plugin.registerCommand(new ChatButtonCommandCompleter(plugin));
        bosterChatCommand = plugin.registerCommand(new BosterChatCommand(plugin));

        chatColorCommand = getCommand(plugin.config().getSection("ChatColor"), ChatColorCommand.class);
        chatSettingsCommand = getCommand(plugin.config().getSection("ChatSettings"), ChatSettingsCommand.class);

        ChatHandler.registerHandler(new SimpleChatHandler());
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

    public static void loadDirectSettings() {
        ConfigurationSection c = plugin.getDirectSettingsFile().getConfig();
        if(c.getBoolean("Enabled", false)) {
            Chat chat = new Chat(c, "direct-chat");
            chat.clear();

            ConfigurationSection sender = c.getSection("Sender");
            if(sender == null) {
                plugin.log("&fCould not load direct settings. Required rows section: &f'&cSender&f'", LogType.WARNING);
                return;
            }

            ConfigurationSection receiver = c.getSection("Receiver");
            if(receiver == null) {
                plugin.log("&fCould not load direct settings. Required rows section: &f'&cReceiver&f'", LogType.WARNING);
                return;
            }

            chat.loadRows(sender, "Sender");
            chat.loadRows(receiver, "Receiver");

            ConfigurationSection console = c.getSection("Console");
            if(console != null) {
                chat.loadRows(console, "Console");
            }

            ChatHandler.setDirectMessageHandler(new SimpleDMHandler(chat));

            getCommand(c.getSection("Commands.Message"), DirectMessageCommand.class);
            getCommand(c.getSection("Commands.Reply"), ReplyCommand.class);
            getCommand(c.getSection("Commands.Ignore"), IgnoreCommand.class);
            getCommand(c.getSection("Commands.UnIgnore"), UnIgnoreCommand.class);
        } else {
            if(directMessageCommand != null) {
                directMessageCommand.unregister();
                directMessageCommand = null;
            }
            if(replyCommand != null) {
                replyCommand.unregister();
                replyCommand = null;
            }
            if(ignoreCommand != null) {
                ignoreCommand.unregister();
                ignoreCommand = null;
            }
            if(unIgnoreCommand != null) {
                unIgnoreCommand.unregister();
                unIgnoreCommand = null;
            }
            ChatHandler.setDirectMessageHandler(null);
        }
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
            connectedDatabase = null;
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

    private static <T extends ChatCommandWrapper> RegisteredCommand getCommand(@NotNull ConfigurationSection section, @NotNull Class<T> clazz) {
        if(!section.getBoolean("Enabled", false)) return null;
        if(section.getString("name") == null) return null;

        try {
            return plugin.registerCommand(clazz.getDeclaredConstructor(BosterChatPlugin.class, ConfigurationSection.class)
                    .newInstance(plugin, section));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
