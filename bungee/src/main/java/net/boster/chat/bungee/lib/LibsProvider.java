package net.boster.chat.bungee.lib;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public class LibsProvider {

    private static ClansSupport clansSupport;

    public static void load() {
        //TODO
    }

    public static String getClan(@NotNull ProxiedPlayer p) {
        return clansSupport != null ? clansSupport.getClan(p) : null;
    }
}
