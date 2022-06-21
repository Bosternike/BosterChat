package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ColorPattern implements MessagePattern {

    @Getter @NotNull private final String color;

    public ColorPattern(@NotNull String color) {
        String s = color;
        if(!s.startsWith("§") && !s.startsWith("&")) {
            s = "&" + s;
        }

        this.color = ChatUtils.toColor(s);
    }

    public ColorPattern(@NotNull ChatColor color) {
        this.color = color.toString();
    }

    @Override
    public @NotNull String process(@NotNull String s) {
        return color + s;
    }

    @Override
    public @NotNull String getInitialPattern() {
        return color;
    }
}
