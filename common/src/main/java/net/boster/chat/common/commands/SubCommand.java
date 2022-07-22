package net.boster.chat.common.commands;

import lombok.Getter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.BosterChatPlugin;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class SubCommand {

    @Getter @NotNull private final String name;
    @Getter @NotNull private final String[] aliases;
    @Getter @NotNull protected final BosterChatPlugin plugin;
    @Getter private boolean permissionRequired;

    public SubCommand(@NotNull String name, @NotNull String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.plugin = BosterChat.get();
    }

    public SubCommand setPermissionRequired(boolean b) {
        permissionRequired = b;
        return this;
    }

    public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args);

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }

    public boolean checkPermission(@NotNull CommandSender sender) {
        if(!hasPermission(sender)) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(plugin.config().getString("Messages.noPermission")));
            return false;
        } else {
            return true;
        }
    }

    public boolean hasPermission(@NotNull CommandSender sender) {
        return !permissionRequired || sender.hasPermission("boster.chat.command." + name);
    }

    public void sendUsage(@NotNull CommandSender sender) {
        String s = plugin.config().getString("Messages." + name + ".usage");
        if(s != null) {
            sender.sendMessage(ChatUtils.toColorAndPrefix(s));
        }
    }
}
