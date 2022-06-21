package net.boster.chat.bungee.data;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bungee.BosterChatBungee;
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
import net.md_5.bungee.BungeeTitle;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerData implements PlayerSender {

    private static final Map<ProxiedPlayer, PlayerData> hash = new HashMap<>();

    @Getter @Setter @NotNull private List<String> recentMessages = new ArrayList<>();

    @Getter private final ProxiedPlayer player;
    @Getter @NotNull private final Cooldowns cooldowns;

    @Getter @Setter @NotNull private ConfigurationSection data;
    @Getter @Setter @Nullable private MessagePattern messagePattern;

    public PlayerData(@NotNull ProxiedPlayer p) {
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

    public static @NotNull PlayerData get(@NotNull ProxiedPlayer p) {
        PlayerData data = hash.get(p);
        if(data == null) {
            data = new PlayerData(p);
        }
        return data;
    }

    @Override
    public boolean isOnline() {
        return player.isConnected();
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
        player.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(TextComponent... textComponent) {
        player.sendMessage(textComponent);
    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut) {
        BungeeTitle bt = new BungeeTitle();
        bt.title(new TextComponent(title));
        bt.subTitle(new TextComponent(subTitle));
        bt.fadeIn(fadeIn);
        bt.stay(stay);
        bt.fadeOut(fadeOut);
        player.sendTitle(bt);
    }

    @Override
    public void sendActionbar(@NotNull String message) {
        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return player.hasPermission(s);
    }

    @Override
    public void sendToChat(@NotNull Chat chat, @NotNull String message) {
        List<ProxiedPlayer> list = new ArrayList<>();
        if(!chat.getChatSettings().getServers().isEmpty()) {
            for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if(chat.getChatSettings().getServers().contains(p.getServer().getInfo().getName()) && checkPlayer(p, chat)) {
                    list.add(p);
                }
            }
        } else {
            list.addAll(ProxyServer.getInstance().getPlayers());
        }
        for(ChatRow row : chat.getRows()) {
            sendRow(chat, row, message, list);
        }
    }

    @Override
    public @NotNull String getRank() {
        return "";
    }

    @Override
    public void saveData() {
        new DatabaseRunnable().run(() -> {
            data.set("ChatColor", messagePattern != null ? messagePattern.getInitialPattern() : null);

            BosterChat.getDataSetter().save(player.getUniqueId().toString(), data);
        });
    }

    private boolean checkPlayer(ProxiedPlayer p, Chat c) {
        if(c.getChatSettings().getSeeMessagesPermission() == null) return true;

        return p.hasPermission(c.getChatSettings().getSeeMessagesPermission());
    }

    public void sendRow(@NotNull Chat chat, @NotNull ChatRow row, @NotNull String message, @NotNull Collection<? extends ProxiedPlayer> players) {
        List<TextComponent> list = new ArrayList<>();

        for(ChatComponent cc : row.getComponents()) {
            String s = toPlaceholders(cc.getText(), message, chat);
            BaseComponent[] tc = TextComponent.fromLegacyText(s);
            HoverEvent hover = null;
            if(cc.getHover() != null) {
                hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(toPlaceholders(cc.getHover(), message, chat)));
            }
            ClickEvent click = null;
            if(cc.getActionType() != null && cc.getActionString() != null) {
                click = new ClickEvent(cc.getActionType(), toPlaceholders(cc.getActionString(), message, chat));
            }

            for(BaseComponent baseComponent : tc) {
                if (hover != null) {
                    baseComponent.setHoverEvent(hover);
                }
                if (click != null) {
                    baseComponent.setClickEvent(click);
                }
            }

            for(BaseComponent t : tc) {
                list.add((TextComponent) t);
            }
        }

        TextComponent[] tc = list.toArray(new TextComponent[]{});
        for(ProxiedPlayer p : players) {
            p.sendMessage(tc);
        }

        if(chat.getChatSettings().isLogToConsole()) {
            ProxyServer.getInstance().getConsole().sendMessage(tc);
        }
    }

    public String toPlaceholders(@NotNull String s, @NotNull String message, @NotNull Chat chat) {
        String r = ChatUtils.toColor(BosterChatBungee.getInstance().toPlaceholders(this, s, chat));
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
