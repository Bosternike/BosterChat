package net.boster.chat.bukkit.lib.towny;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TownySupport {

    public String getTown(@NotNull Player p) {
        try {
            return TownyAPI.getInstance().getResident(p).getTown().getName();
        } catch (NotRegisteredException e) {
            return null;
        }
    }

    public String getNation(@NotNull Player p) {
        try {
            return TownyAPI.getInstance().getResident(p).getTown().getNation().getName();
        } catch (NotRegisteredException e) {
            return null;
        }
    }
}
