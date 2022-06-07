package net.boster.chat.common.sender;

import org.jetbrains.annotations.NotNull;

public interface CommandSender {

    String getName();

    void sendMessage(String message);

    boolean hasPermission(@NotNull String s);
}
