package net.boster.chat.common.chat.message;

import lombok.*;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatMessageBuilder {

    @Getter @Setter @NotNull private CommandSender sender;
    @Getter @Setter @NotNull private Chat chat;

    @Getter @Setter @NotNull private MessageBoxBuilder box = new MessageBoxBuilder();

    @Getter @Setter @NotNull private Collection<PlayerSender> receivers = new ArrayList<>();

    public @NotNull ChatMessage toMessage() {
        ChatMessage.MessageBox mb = new ChatMessage.MessageBox(box.getInitialMessage());
        for(ChatMessageRowBuilder cb : box.getRows()) {
            mb.getRows().add(new ChatMessage.ChatMessageRow(cb.getMessage(), cb.getComponents()));
        }

        return new ChatMessage(sender, chat, mb, receivers);
    }

    public static class MessageBoxBuilder {
        /**
         * Message that sent was sent from Console/Player...
         */
        @Getter @Setter @NotNull private String initialMessage;

        /**
         * Reformatted message.
         */
        @Getter @Setter @NotNull private List<ChatMessageRowBuilder> rows = new ArrayList<>();

        public void addRow(@NotNull String message, @NotNull TextComponent[] components) {
            rows.add(new ChatMessageRowBuilder(message, components));
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageRowBuilder {
        @Getter @Setter @NotNull private String message;
        @Getter @Setter @NotNull private TextComponent[] components;
    }
}
