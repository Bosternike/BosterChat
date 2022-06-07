package net.boster.chat.common.chat.settings;

import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Setting {

    @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message);
}
