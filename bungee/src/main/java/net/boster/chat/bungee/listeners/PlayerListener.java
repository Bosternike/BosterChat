package net.boster.chat.bungee.listeners;

import net.boster.chat.bungee.data.PlayerData;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.handler.ChatHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent e) {
        if(e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            PlayerData sender = PlayerData.get(p);

            for(Chat c : Chat.chats()) {
                if(!c.startsWithSymbol(e.getMessage())) continue;

                if (c.getChatSettings().getServers().isEmpty() || c.getChatSettings().getServers().contains(p.getServer().getInfo().getName())) {
                    net.boster.chat.common.events.ChatEvent event = new net.boster.chat.common.events.ChatEvent(sender, e.getMessage(), c);
                    ChatHandler.callEvent(event);

                    if(event.isContinued()) continue;

                    if(event.isCancelled()) {
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PostLoginEvent e) {
        new PlayerData(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisconnect(PlayerDisconnectEvent e) {
        PlayerData.get(e.getPlayer()).clear();
    }
}
