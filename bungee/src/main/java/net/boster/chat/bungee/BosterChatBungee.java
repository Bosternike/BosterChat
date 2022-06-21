package net.boster.chat.bungee;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bungee.config.BungeeConfig;
import net.boster.chat.bungee.data.PlayerData;
import net.boster.chat.bungee.files.BosterChatFileImpl;
import net.boster.chat.bungee.files.BosterFile;
import net.boster.chat.bungee.lib.LibsProvider;
import net.boster.chat.bungee.listeners.PlayerListener;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.implementation.ChatPlaceholdersImpl;
import net.boster.chat.common.chat.implementation.SettingsImpl;
import net.boster.chat.common.chat.placeholders.ChatPlaceholders;
import net.boster.chat.common.chat.settings.Settings;
import net.boster.chat.common.commands.ChatCommand;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import net.boster.chat.common.log.ChatLog;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.placeholders.PlaceholdersRequest;
import net.boster.chat.common.provider.ChatLogProvider;
import net.boster.chat.common.provider.PlaceholderProvider;
import net.boster.chat.common.placeholders.PlaceholdersManager;
import net.boster.chat.common.provider.BosterChatProvider;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.UUID;

public class BosterChatBungee extends Plugin implements BosterChatPlugin {

    @Getter private static BosterChatBungee instance;

    @Getter private BosterChatFile configFile;
    @Getter private BosterChatFile chatsFile;

    @Getter @Setter
    @NotNull private ChatLog chatLog;

    private Settings settings;
    private ChatPlaceholders chatPlaceholders;

    @Getter @NotNull private final CommandSender console = new ConsoleSender();

    public void onEnable() {
        instance = this;
        BosterChatProvider.setProvider(this);

        chatLog = new ChatLogProvider(getDataFolder());

        BosterFile configF = new BosterFile("config");
        BosterFile chatsF = new BosterFile("chats");

        String PREFIX = "\u00a76+\u00a7a---------------- \u00a7dBosterChat \u00a7a------------------\u00a76+";
        getProxy().getConsole().sendMessage(new TextComponent(PREFIX));

        LibsProvider.load();

        configF.loadFile(true, true);
        chatsF.loadFile(true, true);

        configFile = new BosterChatFileImpl(configF);
        chatsFile = new BosterChatFileImpl(chatsF);

        settings = new SettingsImpl(configFile.getConfig().getSection("Settings"));
        chatPlaceholders = new ChatPlaceholdersImpl(configFile.getConfig().getSection("Placeholders"));

        BosterChatProvider.enable();

        getProxy().getPluginManager().registerListener(this, new PlayerListener());

        getProxy().getConsole().sendMessage(new TextComponent("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fThe plugin has been \u00a7dEnabled\u00a7f!"));
        getProxy().getConsole().sendMessage(new TextComponent("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fPlugin creator: \u00a7dBosternike"));
        getProxy().getConsole().sendMessage(new TextComponent("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fPlugin version: \u00a7d" + getDescription().getVersion()));
        getProxy().getConsole().sendMessage(new TextComponent(PREFIX));
    }

    public void onDisable() {
        for(PlayerData data : PlayerData.players()) {
            data.saveData();
        }
        BosterChatProvider.disable();
        PlayerData.clearAll();
    }

    @Override
    public void log(@NotNull String s, @NotNull LogType log) {
        getProxy().getConsole().sendMessage(new TextComponent(log.getFormat() + log.getColor() + ChatUtils.toColor(s)));
    }

    @Override
    public @NotNull Settings getSettings() {
        return settings;
    }

    @Override
    public @NotNull ChatPlaceholders getPlaceholders() {
        return chatPlaceholders;
    }

    @Override
    public @NotNull ConfigurationSection config() {
        return configFile.getConfig();
    }

    @Override
    public @NotNull ConfigurationSection chats() {
        return chatsFile.getConfig();
    }

    @Override
    public void registerCommand(@NotNull ChatCommand command) {
        getProxy().getPluginManager().registerCommand(this, new Command(command.getName(), null, command.getAliases()) {
            @Override
            public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
                if(sender instanceof ProxiedPlayer) {
                    command.execute(PlayerData.get((ProxiedPlayer) sender), args);
                } else {
                    command.execute(console, args);
                }
            }
        });
    }

    @Override
    public InputStream getResource(@NotNull String s) {
        return getResourceAsStream(s);
    }

    @Override
    public ConfigurationSection loadConfiguration(@NotNull File file) {
        try {
            return new BungeeConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ConfigurationSection loadConfiguration(@NotNull Reader reader) {
        return new BungeeConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(reader));
    }

    @Override
    public ConfigurationSection loadConfiguration(@NotNull String s) {
        return new BungeeConfig(ConfigurationProvider.getProvider(YamlConfiguration.class).load(s));
    }

    @Override
    public ConfigurationSection emptyConfiguration() {
        return new BungeeConfig(new Configuration());
    }

    @Override
    public BosterChatFile createFile(@NotNull String name, @Nullable String directory) {
        BosterFile f = new BosterFile(name);
        f.setDirectory(directory != null ? directory : "");
        f.loadFile(false, true);
        return new BosterChatFileImpl(f);
    }

    @Override
    public @NotNull Collection<? extends PlayerSender> getPlayers() {
        return PlayerData.players();
    }

    @Override
    public PlayerSender getPlayer(@NotNull String name) {
        ProxiedPlayer p = getProxy().getPlayer(name);
        return p != null ? PlayerData.get(p) : null;
    }

    @Override
    public PlayerSender getPlayer(@NotNull UUID id) {
        ProxiedPlayer p = getProxy().getPlayer(id);
        return p != null ? PlayerData.get(p) : null;
    }

    @Override
    public <T> PlayerSender toSender(@NotNull T t) {
        if(!(t instanceof ProxiedPlayer)) return null;

        return PlayerData.get((ProxiedPlayer) t);
    }

    public @NotNull String toPlaceholders(@NotNull PlayerData sender, @NotNull String message, @NotNull Chat chat) {
        ProxiedPlayer p = sender.getPlayer();
        String r = message.replace("%display_name%", p.getDisplayName())
                .replace("%name%", p.getName());

        String clan = LibsProvider.getClan(p);
        r = r.replace("%clan%", clan != null ? clan : chatPlaceholders.NO_CLAN());

        for(PlaceholderProvider<ProxiedPlayer> pp : PlaceholdersManager.requestPlaceholders(ProxiedPlayer.class)) {
            r = pp.getFunction().apply(new PlaceholdersRequest<>(p, sender, chat, r));
        }

        return r;
    }
}
