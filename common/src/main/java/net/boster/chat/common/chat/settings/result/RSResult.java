package net.boster.chat.common.chat.settings.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public abstract class RSResult {

    @Getter @NotNull protected final List<String> list;

    public abstract @Nullable String result(@NotNull PlayerSender sender, @NotNull String message);
}
