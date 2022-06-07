package net.boster.chat.bukkit;

import lombok.Getter;
import net.boster.chat.bukkit.commands.BosterCommand;
import net.boster.chat.bukkit.config.BukkitConfig;
import net.boster.chat.bukkit.data.PlayerData;
import net.boster.chat.bukkit.files.BosterChatFileImpl;
import net.boster.chat.bukkit.files.BosterFile;
import net.boster.chat.bukkit.lib.ClanSupport;
import net.boster.chat.bukkit.lib.LibsProvider;
import net.boster.chat.bukkit.lib.PAPISupport;
import net.boster.chat.bukkit.lib.VaultSupport;
import net.boster.chat.bukkit.listeners.PlayerListener;
import net.boster.chat.bukkit.utils.VersionManager;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.implementation.ChatPlaceholdersImpl;
import net.boster.chat.common.chat.implementation.SettingsImpl;
import net.boster.chat.common.chat.placeholders.ChatPlaceholders;
import net.boster.chat.common.chat.settings.Settings;
import net.boster.chat.common.commands.ChatCommand;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.files.BosterChatFile;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.provider.PlaceholderProvider;
import net.boster.chat.common.placeholders.PlaceholdersManager;
import net.boster.chat.common.provider.BosterChatProvider;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Reader;

public class BosterChatBukkit extends JavaPlugin implements BosterChatPlugin {

    @Getter private static BosterChatBukkit instance;

    @Getter private BosterChatFile configFile;
    @Getter private BosterChatFile chatsFile;

    private Settings settings;
    private ChatPlaceholders chatPlaceholders;

    @Getter @NotNull private final CommandSender console = new ConsoleSender();

    public void onEnable() {
        instance = this;
        BosterChatProvider.setProvider(this);

        BosterFile configF = new BosterFile("config");
        BosterFile chatsF = new BosterFile("chats");

        String PREFIX = "\u00a76+\u00a7a---------------- \u00a7dBosterChat \u00a7a------------------\u00a76+";

        LibsProvider.load();
        ClanSupport.load();
        VersionManager.load();
        VaultSupport.load();
        PAPISupport.load();

        saveDefaultConfig();
        chatsF.loadFile(true, true);
        configF.loadFile(true, true);

        configFile = new BosterChatFileImpl(configF);
        chatsFile = new BosterChatFileImpl(chatsF);

        settings = new SettingsImpl(configFile.getConfig().getSection("Settings"));
        chatPlaceholders = new ChatPlaceholdersImpl(configFile.getConfig().getSection("Placeholders"));

        BosterChatProvider.enable();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        BosterCommand.load();

        Bukkit.getConsoleSender().sendMessage(PREFIX);
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fThe plugin has been \u00a7dEnabled\u00a7f!");
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fPlugin creator: \u00a7dBosternike");
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterChat\u00a7d] \u00a7fPlugin version: \u00a7d" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(PREFIX);
    }

    public void onDisable() {
        PlayerData.clearAll();
    }

    @Override
    public void log(@NotNull String s, @NotNull LogType log) {
        Bukkit.getConsoleSender().sendMessage(log.getFormat() + log.getColor() + ChatUtils.toColor(s));
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
        new BosterCommand(this, command.getName(), command.getAliases()) {
            @Override
            public boolean execute(org.bukkit.command.@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                if(sender instanceof Player) {
                    command.execute(PlayerData.get((Player) sender), args);
                } else {
                    command.execute(console, args);
                }
                return true;
            }
        }.register();
    }

    @Override
    public ConfigurationSection loadConfiguration(@NotNull File file) {
        return new BukkitConfig(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public ConfigurationSection loadConfiguration(@NotNull Reader reader) {
        return new BukkitConfig(YamlConfiguration.loadConfiguration(reader));
    }

    public @NotNull String toPlaceholders(@NotNull Player p, @NotNull String message) {
        String r = message.replace("%display_name%", p.getDisplayName())
                .replace("%name%", p.getName()).replace("%balance%", Double.toString(VaultSupport.getBalance(p)))
                .replace("%world%", p.getWorld().getName()).replace("%health%", Double.toString(p.getHealth()))
                .replace("%food%", Double.toString(p.getFoodLevel()))
                .replace("%exp%", Double.toString(p.getExp()))
                .replace("%level%", Integer.toString(p.getLevel()))
                .replace("%prefix%", VaultSupport.getPrefix(p))
                .replace("%suffix%", VaultSupport.getSuffix(p));

        String clan = ClanSupport.getClan(p);
        r = r.replace("%clan%", clan != null ? clan : chatPlaceholders.NO_CLAN());

        String town = LibsProvider.getTownyTown(p);
        r = r.replace("%towny_town%", town != null ? town : chatPlaceholders.NO_TOWN());

        String nation = LibsProvider.getTownyNation(p);
        r = r.replace("%towny_nation%", nation != null ? nation : chatPlaceholders.NO_NATION());

        for(PlaceholderProvider<Player> pp : PlaceholdersManager.requestPlaceholders(Player.class)) {
            r = pp.getFunction().apply(p);
        }

        r = PAPISupport.setPlaceholders(p, r);

        return r;
    }
}
