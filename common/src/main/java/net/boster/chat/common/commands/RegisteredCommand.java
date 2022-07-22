package net.boster.chat.common.commands;

import org.jetbrains.annotations.NotNull;

public interface RegisteredCommand {

    @NotNull Object getInstance();
    void unregister();
}
