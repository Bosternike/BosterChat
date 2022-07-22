package net.boster.chat.common.commands;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class ChatCommandWrapper implements ChatCommand {

    @Getter @NotNull protected final String name;
    @Getter @NotNull protected final String[] aliases;
    @Getter @NotNull protected BosterChatPlugin plugin;
    @Getter @Setter @Nullable protected String permission;

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull String name, @NotNull String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
    }

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull ConfigurationSection section) {
        this.plugin = plugin;
        this.name = Objects.requireNonNull(section.getString("name"));
        this.aliases = section.getStringList("aliases").toArray(new String[]{});
        ConfigurationSection perm = section.getSection("PermissionRequirement");
        if(perm != null && perm.getBoolean("Enabled", false)) {
            this.permission = perm.getString("perm");
        }
    }

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull List<String> commands) {
        this.plugin = plugin;
        this.name = commands.get(0);
        commands.remove(0);
        this.aliases = commands.toArray(new String[]{});
    }

    protected boolean checkPermission(@NotNull CommandSender sender) {
        return permission == null || checkPermission(sender, permission);
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
