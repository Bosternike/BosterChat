package net.boster.chat.bukkit.lib;

import net.boster.chat.bukkit.lib.clan.EpicClansSupport;
import net.boster.chat.bukkit.lib.clan.SimpleClansSupport;
import net.boster.chat.bukkit.lib.clan.UltimateClansSupport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClanSupport {

    private static ClansSupport c;

    public static void load() {
        try {
            Class.forName("net.sacredlabyrinth.phaed.simpleclans.SimpleClans");
            c = new SimpleClansSupport();
            return;
        } catch (ReflectiveOperationException ignored) {}

        try {
            Class.forName("me.backstabber.epicsetclans.api.EpicSetClansApi");
            c = new EpicClansSupport();
            return;
        } catch (ReflectiveOperationException ignored) {}

        try {
            Class.forName("me.ulrich.clans.Clans");
            c = new UltimateClansSupport();
            return;
        } catch (ReflectiveOperationException ignored) {}

        try {
            Class.forName("me.ulrich.clans.Clans");

        } catch (ReflectiveOperationException ignored) {}
    }

    public static String getClan(@NotNull Player p) {
        return c != null ? c.getClan(p) : null;
    }

    public static void setClansProvider(@Nullable ClansSupport clansSupport) {
        c = clansSupport;
    }
}
