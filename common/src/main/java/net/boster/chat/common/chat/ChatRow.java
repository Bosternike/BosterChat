package net.boster.chat.common.chat;

import net.boster.chat.common.chat.component.ChatComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ChatRow {

    @NotNull List<ChatComponent> getComponents();
}
