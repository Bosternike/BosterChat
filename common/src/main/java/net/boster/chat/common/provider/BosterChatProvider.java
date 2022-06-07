package net.boster.chat.common.provider;

import com.google.common.collect.ImmutableList;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.commands.BosterChatCommand;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.handler.ChatHandlerWrapper;
import net.boster.chat.common.utils.ConfigUtils;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.CooldownUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;

public class BosterChatProvider {

    private static BosterChatPlugin plugin;

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

    public static void enable() {
        checkConfig();
        loadCooldowns();
        loadChats();
        plugin.registerCommand(new BosterChatCommand(plugin));
        ChatHandler.registerHandler(new ChatHandlerWrapper());
    }

    public static void loadCooldowns() {
        CooldownUtils.load(plugin.config().getSection("CooldownFormat"), plugin.config().getSection("CooldownMessage"));
    }

    public static void checkConfig() {
        ConfigurationSection cfg = plugin.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml")));
        File cf = new File(plugin.getDataFolder(), "config.yml");
        if(!ConfigUtils.hasAllStrings(cfg, plugin.loadConfiguration(cf), ImmutableList.of())) {
            ConfigUtils.replaceOldConfig(cf, cf, plugin.getResource("config.yml"));
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
}
