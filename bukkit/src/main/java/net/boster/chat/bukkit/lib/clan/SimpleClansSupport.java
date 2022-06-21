package net.boster.chat.bukkit.lib.clan;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SimpleClansSupport implements ClansSupport {

    @Override
    public String getClan(@NotNull Player p) {
        Clan c = SimpleClans.getInstance().getClanManager().getClanPlayer(p).getClan();
        return c != null ? c.getName() : null;
    }
}
