package net.boster.chat.common.commands;

import lombok.Getter;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ChatCommandWrapper implements ChatCommand {

    @Getter @NotNull protected final String name;
    @Getter @NotNull protected final String[] aliases;
    @Getter @NotNull protected BosterChatPlugin plugin;

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull String name, @NotNull String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
    }

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        this.plugin = plugin;
        this.name = Objects.requireNonNull(section.getString("name"));
        this.aliases = section.getStringList("aliases").toArray(new String[]{});
    }

    protected boolean checkPermission(@NotNull CommandSender sender, @NotNull String s) {
        if(!sender.hasPermission(s)) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.noPermission")));
            return false;
        } else {
            return true;
        }
    }
}
