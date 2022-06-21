package net.boster.chat.common.utils.colorutils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface ColorUtils {

    String toColor(@NotNull String s);
    ChatColor of(@NotNull String s);
    ChatColor of(@NotNull Color color);
    boolean old();
}
