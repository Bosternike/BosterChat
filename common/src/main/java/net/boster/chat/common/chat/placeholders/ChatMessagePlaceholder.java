package net.boster.chat.common.chat.placeholders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ChatMessagePlaceholder {

    @Getter @NotNull private String placeholder;
    @Getter @NotNull private String replacement;
}
