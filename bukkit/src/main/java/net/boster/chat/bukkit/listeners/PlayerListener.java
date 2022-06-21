package net.boster.chat.bukkit.listeners;

import net.boster.chat.bukkit.data.PlayerData;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.events.ChatEvent;
import net.boster.chat.common.handler.ChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData sender = PlayerData.get(p);
        for(Chat c : Chat.chats()) {
            if(!c.startsWithSymbol(e.getMessage())) continue;

            if(c.getChatSettings().getWorlds().isEmpty() || c.getChatSettings().getWorlds().contains(p.getWorld().getName())) {
                ChatEvent event = new ChatEvent(sender, e.getMessage(), c);
                ChatHandler.callEvent(event);

                if(event.isContinued()) continue;

                if(event.isCancelled()) {
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent e) {
        new PlayerData(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        PlayerData d = PlayerData.get(e.getPlayer());
        d.saveData();
        d.clear();
    }
}
