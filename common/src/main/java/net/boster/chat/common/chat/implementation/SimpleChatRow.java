package net.boster.chat.common.chat.implementation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SimpleChatRow implements ChatRow {

    @Getter @NotNull private final List<ChatComponent> components;
}
