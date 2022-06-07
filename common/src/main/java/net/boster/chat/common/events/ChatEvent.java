package net.boster.chat.common.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ChatEvent {

    @Getter @NotNull private final PlayerSender sender;
    @Getter @NotNull private final String message;
    @Getter @NotNull private final Chat chat;

    @Getter @Setter private boolean continued;

    @Getter @Setter private boolean cancelled;
}
