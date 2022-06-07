package net.boster.chat.bukkit.utils.actionbar;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ActionBarSender {

    void send(@NotNull Player p, @NotNull String message);
}
