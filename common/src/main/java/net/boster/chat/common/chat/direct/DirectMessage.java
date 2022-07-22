package net.boster.chat.common.chat.direct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DirectMessage {

    @Getter @NotNull private final PlayerSender sender;
    @Getter @NotNull private final PlayerSender receiver;
    @Getter @NotNull private final String message;
}
