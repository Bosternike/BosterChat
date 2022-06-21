package net.boster.chat.common.chat.pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PatternReader {

    @Nullable MessagePattern read(@NotNull String s);
}
