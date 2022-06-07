package net.boster.chat.bukkit.utils.hover;

import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

public interface HoverBuilder {

    @NotNull HoverEvent build(@NotNull String s);
}
