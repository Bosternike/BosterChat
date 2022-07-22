package net.boster.chat.common.handler;

import net.boster.chat.common.events.DirectMessageEvent;
import org.jetbrains.annotations.NotNull;

public interface DirectMessageHandler {

    void onEvent(@NotNull DirectMessageEvent event);
}
