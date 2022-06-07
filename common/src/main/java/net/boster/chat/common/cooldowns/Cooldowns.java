package net.boster.chat.common.cooldowns;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Cooldowns {
	
	private static final HashMap<UUID, Cooldowns> hash = new HashMap<>();
	
	public final HashMap<String, Long> cooldowns = new HashMap<>();
	
	@Getter @NotNull private final UUID id;
	
	public Cooldowns(@NotNull UUID uuid) {
		hash.put(uuid, this);
		this.id = uuid;
	}
	
	public static Cooldowns get(@NotNull UUID uuid) {
		return hash.get(uuid);
	}
	
	public void addCooldown(@NotNull String k, long l) {
		cooldowns.put(k, l);
	}
	
	public long getCooldown(@NotNull String s, int time) {
		if(!cooldowns.containsKey(s)) return 0;

		return ((getCooldown(s) / 1000) + time) - (System.currentTimeMillis() / 1000);
	}

	public boolean hasCooldown(@NotNull String s) {
		return cooldowns.containsKey(s);
	}
	
	public long getCooldown(@NotNull String s) {
		return cooldowns.getOrDefault(s, 0L);
	}
	
	public void removeCooldown(@NotNull String s) {
		cooldowns.remove(s);
	}
	
	public void clear() {
		hash.remove(id);
	}

}
