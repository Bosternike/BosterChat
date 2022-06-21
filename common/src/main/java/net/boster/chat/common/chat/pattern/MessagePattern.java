package net.boster.chat.common.chat.pattern;

import org.jetbrains.annotations.NotNull;

public interface MessagePattern {

    @NotNull String process(@NotNull String s);
    @NotNull String getInitialPattern();
}
