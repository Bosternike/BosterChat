package net.boster.chat.bukkit;

import net.boster.chat.common.sender.CommandSender;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ConsoleSender implements CommandSender {

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return true;
    }
}
