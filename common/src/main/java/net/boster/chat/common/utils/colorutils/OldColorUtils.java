package net.boster.chat.common.utils.colorutils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class OldColorUtils implements ColorUtils {

    @Override
    public String toColor(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
