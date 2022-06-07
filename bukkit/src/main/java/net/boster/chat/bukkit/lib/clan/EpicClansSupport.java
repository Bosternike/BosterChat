package net.boster.chat.bukkit.lib.clan;

import me.backstabber.epicsetclans.api.EpicSetClansApi;
import me.backstabber.epicsetclans.api.data.ClanData;
import net.boster.chat.bukkit.lib.ClansSupport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EpicClansSupport implements ClansSupport {

    @Override
    public String getClan(@NotNull Player p) {
        ClanData data = EpicSetClansApi.getClanManager().getClanData(p);
        return data != null ? data.getClanName() : null;
    }
}
