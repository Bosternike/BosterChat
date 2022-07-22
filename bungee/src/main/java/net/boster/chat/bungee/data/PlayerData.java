package net.boster.chat.bungee.data;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bungee.BosterChatBungee;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.button.ButtonBoundActions;
import net.boster.chat.common.button.SimpleButtonBoundActions;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.chat.direct.DirectSession;
import net.boster.chat.common.chat.direct.DirectSettings;
import net.boster.chat.common.chat.message.ChatMessage;
import net.boster.chat.common.chat.message.ChatMessageBuilder;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
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
import java.util.stream.Collectors;

public class PlayerData implements PlayerSender {

    private static final Map<ProxiedPlayer, PlayerData> hash = new HashMap<>();

    @Getter @Setter @NotNull private List<String> recentMessages = new ArrayList<>();

    @Getter private final ProxiedPlayer player;
    @Getter @NotNull private final Cooldowns cooldowns;

    @Getter @Setter @NotNull private ButtonBoundActions buttonBoundActions = new SimpleButtonBoundActions();

    @Getter private boolean dataLoaded = false;

    @Getter @Setter @NotNull private ConfigurationSection data;
    @Getter @Setter @Nullable private MessagePattern messagePattern;
    @Getter @Setter @NotNull private List<String> disabledChats = new ArrayList<>();
    @Getter @NotNull private final DirectSession directSession;
    @Getter @NotNull private final DirectSettings directSettings;

    public PlayerData(@NotNull ProxiedPlayer p) {
        this.player = p;
        this.cooldowns = new Cooldowns(p.getUniqueId());
        this.data = BosterChat.get().emptyConfiguration();
        this.directSession = new DirectSession(this);
        this.directSettings = new DirectSettings(this);

        loadData();

        hash.put(p, this);

    }

    private void loadData() {
        new DatabaseRunnable().run(() -> {
            data = BosterChat.getDataSetter().configuration(player.getUniqueId().toString());

            disabledChats = data.getStringList("DisabledChats");

            String m = data.getString("ChatColor");
            if(m != null) {
                try {
                    messagePattern = ChatUtils.getPatternReader().read(m);
                } catch (Exception ignored) {}
            }

            ConfigurationSection direct = data.getSection("Direct");
            if(direct != null) {
                directSettings.setEnabled(direct.getBoolean("Enabled", true));
                directSettings.setIgnoring(direct.getStringList("Ignoring"));

                ConfigurationSection de = direct.getSection("EnabledMap");
                if(de != null) {
                    Map<String, Boolean> map = new HashMap<>();
                    for(String k : de.getKeys()) {
                        map.put(k, de.getBoolean(k));
                    }
                    directSettings.setEnabledMap(map);
                }
            }

            dataLoaded = true;
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
    public @NotNull ChatMessage sendToChat(@NotNull Chat chat, @NotNull String message) {
        ChatMessageBuilder mb = new ChatMessageBuilder();
        mb.setSender(this);
        mb.setChat(chat);
        mb.getBox().setInitialMessage(message);

        List<PlayerData> list = ProxyServer.getInstance().getPlayers().stream().map(PlayerData::get)
                .filter(p -> !p.getDisabledChats().contains(chat.getName())).collect(Collectors.toList());

        if(!chat.getChatSettings().getServers().isEmpty()) {
            for(PlayerData p : list) {
                if(chat.getChatSettings().getServers().contains(p.getPlayer().getServer().getInfo().getName()) && checkPlayer(p.getPlayer(), chat)) {
                    mb.getReceivers().add(p);
                }
            }
        } else {
            mb.getReceivers().addAll(list);
        }

        for(ChatRow row : chat.getRows()) {
            mb.getBox().getRows().add(sendRow(chat, message, mb.getReceivers(), row));
        }

        return mb.toMessage();
    }

    @Override
    public @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> players, boolean b,
                                       @Nullable List<ChatMessagePlaceholder> placeholders, @NotNull ChatRow... rows) {
        ChatMessageBuilder mb = new ChatMessageBuilder();
        mb.setSender(this);
        mb.setChat(chat);
        mb.getReceivers().addAll(players);
        mb.getBox().setInitialMessage(message);

        for(ChatRow row : rows) {
            StringBuilder sb = new StringBuilder();
            List<TextComponent> list = new ArrayList<>();

            for(ChatComponent cc : row.getComponents()) {
                String s = toPlaceholders(cc.getText(), message, chat, placeholders);
                sb.append(s);
                BaseComponent[] tc = TextComponent.fromLegacyText(s);

                applyCH(cc, message, chat, tc);

                for(BaseComponent t : tc) {
                    list.add((TextComponent) t);
                }
            }

            TextComponent[] tc = list.toArray(new TextComponent[]{});
            for(PlayerSender p : players) {
                p.sendMessage(tc);
            }

            if(b && chat.getChatSettings().isLogToConsole()) {
                ProxyServer.getInstance().getConsole().sendMessage(tc);
            }

            mb.getBox().addRow(sb.toString(), tc);
        }

        return mb.toMessage();
    }

    @Override
    public @NotNull String getRank() {
        return "";
    }

    @Override
    public void saveData() {
        if(!dataLoaded) return;

        data.set("DisabledChats", disabledChats);
        data.set("ChatColor", messagePattern != null ? messagePattern.getInitialPattern() : null);

        data.set("Direct.Enabled", directSettings.isEnabled());
        data.set("Direct.Ignoring", directSettings.getIgnoring());
        for(Map.Entry<String, Boolean> ke : directSettings.getEnabledMap().entrySet()) {
            data.set("Direct.EnabledMap." + ke.getKey(), ke.getValue());
        }

        BosterChat.getDataSetter().save(player.getUniqueId().toString(), data);
    }

    private boolean checkPlayer(ProxiedPlayer p, Chat c) {
        if(c.getChatSettings().getSeeMessagesPermission() == null) return true;

        return p.hasPermission(c.getChatSettings().getSeeMessagesPermission());
    }

    public @NotNull ChatMessageBuilder.ChatMessageRowBuilder sendRow(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> players, @NotNull ChatRow... rows) {
        ChatMessageBuilder.ChatMessageRowBuilder c = new ChatMessageBuilder.ChatMessageRowBuilder();

        for(ChatRow row : rows) {
            StringBuilder sb = new StringBuilder();
            List<TextComponent> list = new ArrayList<>();

            for(ChatComponent cc : row.getComponents()) {
                String s = toPlaceholders(cc.getText(), message, chat, null);
                sb.append(s);
                BaseComponent[] tc = TextComponent.fromLegacyText(s);

                applyCH(cc, message, chat, tc);

                for(BaseComponent t : tc) {
                    list.add((TextComponent) t);
                }
            }

            TextComponent[] tc = list.toArray(new TextComponent[]{});
            for(PlayerSender p : players) {
                p.sendMessage(tc);
            }

            if(chat.getChatSettings().isLogToConsole()) {
                ProxyServer.getInstance().getConsole().sendMessage(tc);
            }

            c.setMessage(sb.toString());
            c.setComponents(tc);
        }

        return c;
    }

    private void applyCH(ChatComponent cc, String message, Chat chat, BaseComponent... tc) {
        HoverEvent hover = null;
        if(cc.getHover() != null) {
            hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(toPlaceholders(cc.getHover(), message, chat, null)));
        }
        ClickEvent click = null;
        if(cc.getActionType() != null && cc.getActionString() != null) {
            click = new ClickEvent(cc.getActionType(), toPlaceholders(cc.getActionString(), message, chat, null));
        }

        for(int i = 0; i < tc.length; i++) {
            if(hover != null) {
                tc[i].setHoverEvent(hover);
            }
            if(click != null) {
                tc[i].setClickEvent(click);
            }
        }
    }

    public String toPlaceholders(@NotNull String s, @NotNull String message, @NotNull Chat chat, @Nullable List<ChatMessagePlaceholder> placeholders) {
        String r = ChatUtils.toColor(BosterChatBungee.getInstance().toPlaceholders(this, s, chat));
        r = r.replace("%message%", message).replace("%colored_message%", ChatUtils.toColor(message));

        if(placeholders != null) {
            for(ChatMessagePlaceholder cmp : placeholders) {
                r = r.replace(cmp.getPlaceholder(), cmp.getReplacement());
            }
        }

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
