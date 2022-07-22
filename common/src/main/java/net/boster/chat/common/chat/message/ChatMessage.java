package net.boster.chat.common.chat.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Getter @NotNull private final CommandSender sender;
    @Getter @NotNull private final Chat chat;

    @Getter @NotNull private final MessageBox box;

    @Getter @NotNull private Collection<PlayerSender> receivers = new ArrayList<>();

    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class MessageBox {
        /**
         * Message that sent was sent from Console/Player...
         */
        @Getter @NotNull private final String initialMessage;

        /**
         * Reformatted message.
         */
        @Getter @NotNull private List<ChatMessageRow> rows = new ArrayList<>();
    }

    @RequiredArgsConstructor
    public static class ChatMessageRow {
        @Getter @NotNull private final String message;
        @Getter @NotNull private final TextComponent[] components;
    }
}
