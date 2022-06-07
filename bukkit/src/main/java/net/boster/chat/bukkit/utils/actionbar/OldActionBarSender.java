package net.boster.chat.bukkit.utils.actionbar;

import net.boster.chat.bukkit.utils.ReflectionUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OldActionBarSender implements ActionBarSender {

    @Override
    public void send(@NotNull Player p, @NotNull String message) {
        ReflectionUtils.sendActionbar(p, message);
    }
}
