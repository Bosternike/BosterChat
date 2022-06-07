package net.boster.chat.bungee;

import net.boster.chat.common.sender.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class ConsoleSender implements CommandSender {

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(message));
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return true;
    }
}
