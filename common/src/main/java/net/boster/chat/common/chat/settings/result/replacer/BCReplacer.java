package net.boster.chat.common.chat.settings.result.replacer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BCReplacer {

    @NotNull String replace(@NotNull String message, @NotNull List<String> list, @NotNull String replacer);
}
