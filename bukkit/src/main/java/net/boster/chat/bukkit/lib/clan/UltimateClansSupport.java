package net.boster.chat.bukkit.lib.clan;

import me.ulrich.clans.Clans;
import me.ulrich.clans.data.ClanData;
import net.boster.chat.bukkit.lib.ClansSupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UltimateClansSupport implements ClansSupport {

    private final Clans clans;

    public UltimateClansSupport() {
        this.clans = (Clans) Bukkit.getPluginManager().getPlugin("UltimateClans");
    }

    @Override
    public String getClan(@NotNull Player p) {
        ClanData data = clans.getClanAPI().getClan(p.getUniqueId());
        return data != null ? data.getTag() : null;
    }
}
