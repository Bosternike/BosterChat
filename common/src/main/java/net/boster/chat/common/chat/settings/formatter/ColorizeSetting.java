package net.boster.chat.common.chat.settings.formatter;

import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorizeSetting extends ChatFormatterSetting {

    public ColorizeSetting(@NotNull ConfigurationSection section) {
        super(section);
    }

    @Override
    public @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message) {
        if(!enabled) return message;
        if(!hasPermission(sender)) return message;

        return ChatUtils.toColor(message);
    }
}
