package net.boster.chat.bukkit.utils.title;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TitleSender {

    void send(@NotNull Player p, @NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut);
}
