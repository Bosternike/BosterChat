package net.boster.chat.common.utils.colorutils;

import net.boster.chat.common.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class OldColorUtils implements ColorUtils {

    @Override
    public String toColor(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public ChatColor of(@NotNull String s) {
        return ChatColor.valueOf(s);
    }

    @Override
    public ChatColor of(@NotNull Color color) {
        return ChatUtils.getClosestColor(color);
    }

    @Override
    public boolean old() {
        return true;
    }

}
