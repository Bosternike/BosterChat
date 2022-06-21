package net.boster.chat.bukkit.data;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bukkit.BosterChatBukkit;
import net.boster.chat.bukkit.lib.VaultSupport;
import net.boster.chat.bukkit.utils.VersionManager;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.Cooldowns;
import net.boster.chat.common.data.database.DatabaseRunnable;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerData implements PlayerSender {

    private static final Map<Player, PlayerData> hash = new HashMap<>();

    @Getter @Setter @NotNull private List<String> recentMessages = new ArrayList<>();

    @Getter private final Player player;
    @Getter @NotNull private final Cooldowns cooldowns;

    @Getter @Setter @NotNull private ConfigurationSection data;
    @Getter @Setter @Nullable private MessagePattern messagePattern;

    public PlayerData(@NotNull Player p) {
        this.player = p;
        this.cooldowns = new Cooldowns(p.getUniqueId());
        this.data = BosterChat.get().emptyConfiguration();

        loadData();

        hash.put(p, this);
    }

    private void loadData() {
        new DatabaseRunnable().run(() -> {
            data = BosterChat.getDataSetter().configuration(player.getUniqueId().toString());

            String m = data.getString("ChatColor");
            if(m != null) {
                try {
                    messagePattern = ChatUtils.getPatternReader().read(m);
                } catch (Exception ignored) {}
            }
        });
    }

    public static PlayerData get(@NotNull Player p) {
        PlayerData data = hash.get(p);
        if(data == null) {
            data = new PlayerData(p);
        }
        return data;
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(TextComponent... textComponent) {
        player.spigot().sendMessage(textComponent);
    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut) {
        VersionManager.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendActionbar(@NotNull String message) {
        VersionManager.sendActionBar(player, message);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return player.hasPermission(s);
    }

    @Override
    public void sendToChat(@NotNull Chat chat, @NotNull String message) {
        List<Player> list = new ArrayList<>();
        if(chat.getChatSettings().getShowDistance() <= -1) {
            if(!chat.getChatSettings().getWorlds().isEmpty()) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(chat.getChatSettings().getWorlds().contains(p.getWorld().getName()) && checkPlayer(p, chat)) {
                        list.add(p);
                    }
                }
            } else {
                list.addAll(Bukkit.getOnlinePlayers());
            }
        } else {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= chat.getChatSettings().getShowDistance() &&
                        checkPlayer(p, chat)) {
                    list.add(p);
                }
            }
        }
        for(ChatRow row : chat.getRows()) {
            sendRow(chat, row, message, list);
        }
    }

    @Override
    public @NotNull String getRank() {
        return VaultSupport.getRank(player);
    }

    @Override
    public void saveData() {
        new DatabaseRunnable().run(() -> {
            data.set("ChatColor", messagePattern != null ? messagePattern.getInitialPattern() : null);

            BosterChat.getDataSetter().save(player.getUniqueId().toString(), data);
        });
    }

    private boolean checkPlayer(Player p, Chat c) {
        if(c.getChatSettings().getSeeMessagesPermission() == null) return true;

        return p.hasPermission(c.getChatSettings().getSeeMessagesPermission());
    }

    public void sendRow(@NotNull Chat chat, @NotNull ChatRow row, @NotNull String message, @NotNull Collection<? extends Player> players) {
        StringBuilder sb = new StringBuilder();
        List<TextComponent> list = new ArrayList<>();

        for(ChatComponent cc : row.getComponents()) {
            String s = toPlaceholders(cc.getText(), message, chat);
            sb.append(s);
            BaseComponent[] tc = TextComponent.fromLegacyText(s);
            HoverEvent hover = null;
            if(cc.getHover() != null) {
                hover = VersionManager.buildHover(toPlaceholders(cc.getHover(), message, chat));
            }
            ClickEvent click = null;
            if(cc.getActionType() != null && cc.getActionString() != null) {
                click = new ClickEvent(cc.getActionType(), toPlaceholders(cc.getActionString(), message, chat));
            }

            for(int i = 0; i < tc.length; i++) {
                if(hover != null) {
                    tc[i].setHoverEvent(hover);
                }
                if(click != null) {
                    tc[i].setClickEvent(click);
                }
            }

            for(BaseComponent t : tc) {
                list.add((TextComponent) t);
            }
        }

        for(Player p : players) {
            p.spigot().sendMessage(list.toArray(new BaseComponent[]{}));
        }

        if(chat.getChatSettings().isLogToConsole()) {
            Bukkit.getConsoleSender().sendMessage(sb.toString());
        }
    }

    public String toPlaceholders(@NotNull String s, @NotNull String message, @NotNull Chat chat) {
        String r = ChatUtils.toColor(BosterChatBukkit.getInstance().toPlaceholders(this, s, chat));
        r = r.replace("%message%", message).replace("%colored_message%", ChatUtils.toColor(message));
        return r;
    }

    public void clear() {
        hash.remove(player);
    }

    public static void clearAll() {
        hash.clear();
    }

    public static @NotNull Collection<PlayerData> players() {
        return hash.values();
    }
}
