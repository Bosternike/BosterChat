package net.boster.chat.bukkit.lib;

import net.boster.chat.bukkit.lib.towny.TownySupport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LibsProvider {

    private static TownySupport townySupport;

    public static void load() {
        try {
            Class.forName("com.palmergames.bukkit.towny.TownyAPI");
            townySupport = new TownySupport();
        } catch (ReflectiveOperationException ignored) {}

        loadFactions();
    }

    private static void loadFactions() {

    }

    public static String getTownyTown(@NotNull Player p) {
        return townySupport != null ? townySupport.getTown(p) : null;
    }

    public static String getTownyNation(@NotNull Player p) {
        return townySupport != null ? townySupport.getNation(p) : null;
    }
}
