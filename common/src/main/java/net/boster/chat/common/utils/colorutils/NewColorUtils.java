package net.boster.chat.common.utils.colorutils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewColorUtils implements ColorUtils {

    private final Pattern pattern;

    public NewColorUtils() {
        this.pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
    }

    @Override
    public String toColor(@NotNull String s) {
        String str = s;
        Matcher match = pattern.matcher(str);

        while(match.find()) {
            String color = s.substring(match.start() + 1, match.end());
            str = str.replace("&" + color, ChatColor.of(color) + "");
            match = pattern.matcher(str);
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public ChatColor of(@NotNull String s) {
        return ChatColor.of(s);
    }

    @Override
    public ChatColor of(@NotNull Color color) {
        return ChatColor.of(color);
    }

    @Override
    public boolean old() {
        return false;
    }
}
