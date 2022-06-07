package net.boster.chat.bukkit.utils.hover;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

public class OldHoverBuilder implements HoverBuilder {

    @Override
    public @NotNull HoverEvent build(@NotNull String s) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(s).create());
    }
}
