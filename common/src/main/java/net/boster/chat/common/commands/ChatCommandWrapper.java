package net.boster.chat.common.commands;

import lombok.Getter;
import net.boster.chat.common.BosterChatPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class ChatCommandWrapper implements ChatCommand {

    @Getter @NotNull protected final String name;
    @Getter @NotNull protected final String[] aliases;
    @Getter @NotNull protected BosterChatPlugin plugin;

    public ChatCommandWrapper(@NotNull BosterChatPlugin plugin, @NotNull String name, @NotNull String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
    }
}
