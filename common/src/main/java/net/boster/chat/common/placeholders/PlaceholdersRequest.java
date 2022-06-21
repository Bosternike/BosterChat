package net.boster.chat.common.placeholders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlaceholdersRequest<T> {

    @Getter @NotNull private final T senderObject;
    @Getter @NotNull private final PlayerSender sender;
    @Getter @NotNull private final Chat chat;
    @Getter @NotNull private final String message;
}
