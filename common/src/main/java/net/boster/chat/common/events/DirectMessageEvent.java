package net.boster.chat.common.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DirectMessageEvent {

    @Getter @NotNull private final CommandSender sender;
    @Getter @NotNull private final PlayerSender receiver;
    @Getter @NotNull private final String message;
    @Getter private final boolean reply;

    @Getter @Setter private boolean cancelled;
}
