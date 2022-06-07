package net.boster.chat.bukkit.utils.hover;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.NotNull;

public class NewHoverBuilder implements HoverBuilder {

    @Override
    public @NotNull HoverEvent build(@NotNull String s) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(s));
    }
}
