package net.boster.chat.bukkit.lib;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultSupport {

	private static boolean isLoaded = false;

	private static Economy eco = null;
	private static Chat chat = null;
	
	public static void load() {
		try {
			Class.forName("net.milkbowl.vault.economy.Economy");
			isLoaded = true;
		} catch (ReflectiveOperationException e) {
			return;
		}

		try {
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			eco = rsp.getProvider();
		} catch (NoClassDefFoundError | NullPointerException ignored) {}
		try {
			RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
			chat = rsp.getProvider();
		} catch (NoClassDefFoundError | NullPointerException ignored) {}
    }

	public static double getBalance(Player p) {
		if(!isLoaded) return 0;

		if(eco != null) {
			return eco.getBalance(p);
		}

		return 0;
	}

	public static String getPrefix(Player p) {
		if(!isLoaded) return "";

		if(chat != null) {
			return chat.getPlayerPrefix(p);
		}

		return "";
	}

	public static String getSuffix(Player p) {
		if(!isLoaded) return "";

		if(chat != null) {
			return chat.getPlayerSuffix(p);
		}

		return "";
	}
}
