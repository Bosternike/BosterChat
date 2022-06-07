package net.boster.chat.bukkit.utils.actionbar;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NewActionBarSender implements ActionBarSender {

    @Override
    public void send(@NotNull Player p, @NotNull String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
