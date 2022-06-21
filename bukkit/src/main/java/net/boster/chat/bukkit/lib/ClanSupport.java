package net.boster.chat.bukkit.lib;

import net.boster.chat.bukkit.lib.clan.ClansSupport;
import net.boster.chat.bukkit.lib.clan.SimpleClansSupport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClanSupport {

    private static ClansSupport c;

    public static void load() {
        try {
            Class.forName("net.sacredlabyrinth.phaed.simpleclans.SimpleClans");
            c = new SimpleClansSupport();
        } catch (ReflectiveOperationException ignored) {}
    }

    public static String getClan(@NotNull Player p) {
        return c != null ? c.getClan(p) : null;
    }

    public static void setClansProvider(@Nullable ClansSupport clansSupport) {
        c = clansSupport;
    }
}
