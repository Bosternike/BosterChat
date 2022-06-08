package net.boster.chat.common.chat;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.implementation.SettingsImpl;
import net.boster.chat.common.chat.settings.Settings;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.cooldowns.Cooldown;
import net.boster.chat.common.cooldowns.CooldownUtils;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Chat {

    private static final Map<String, Chat> map = new HashMap<>();
    private static List<Chat> list = new ArrayList<>();

    @Getter @NotNull private final String name;
    @Getter @Setter @NotNull private ChatSettings chatSettings;
    @Getter @Setter @NotNull private Settings settings;
    @Getter @Setter @Nullable private Cooldown cooldown;

    @Getter @Setter @NotNull private List<ChatRow> rows = new ArrayList<>();
    @Getter @Setter @NotNull private Map<String, String> rankColorMap = new HashMap<>();
    @Getter @Setter @NotNull private Map<String, String> replacesMap = new HashMap<>();

    public Chat(@NotNull String name) {
        this.name = name;

        map.put(name, this);
        list.add(this);
    }

    public Chat(@NotNull ConfigurationSection section, @NotNull String name) {
        this(name);

        this.chatSettings = new ChatSettings(section);

        ConfigurationSection s = section.getSection("Settings");
        if(s != null) {
            this.settings = new SettingsImpl(s);
        } else {
            this.settings = BosterChat.get().getSettings().clone();
        }

        ConfigurationSection r = section.getSection("rows");
        if(r != null) {
            for(String rs : r.getKeys()) {
                rows.add(new ChatRow(r.getSection(rs), rs));
            }
        }

        ConfigurationSection cd = section.getSection("Cooldown");
        if(cd != null) {
            this.cooldown = new Cooldown(cd);
        }

        ConfigurationSection cr = section.getSection("Replaces");
        if(cr != null) {
            for(String crs : cr.getKeys()) {
                replacesMap.put(crs, cr.getString(crs));
            }
        }

        ConfigurationSection rc = section.getSection("RankColors");
        if(rc != null) {
            for(String rs : rc.getKeys()) {
                rankColorMap.put(rs, rc.getString(rs));
            }
        }
    }

    public boolean startsWithSymbol(@NotNull String message) {
        return chatSettings.getSymbol() == null || message.startsWith(chatSettings.getSymbol());
    }

    public @Nullable String removeStartSymbol(@NotNull String message) {
        if(chatSettings.getSymbol() == null) return message;

        String msg = message.replaceFirst(chatSettings.getSymbol(), "");

        return !msg.isEmpty() ? msg : null;
    }

    public boolean checkPermission(@NotNull PlayerSender sender) {
        if(chatSettings.getPermission() == null) return true;

        if(!sender.hasPermission(chatSettings.getPermission().getPermission())) {
            if(chatSettings.getPermission().getMessage() != null) {
                sender.sendMessage(chatSettings.getPermission().getMessage());
            }
            return false;
        } else {
            return true;
        }
    }

    public @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message) {
        String r = message;
        if(settings.getDeniedContentsSetting() != null) {
            r = settings.getDeniedContentsSetting().checkMessage(sender, r);
        }
        if(r != null && settings.getDeniedWordsSetting() != null) {
            r = settings.getDeniedWordsSetting().checkMessage(sender, r);
        }
        if(r != null && settings.getDenyAddressSetting() != null) {
            r = settings.getDenyAddressSetting().checkMessage(sender, r);
        }
        if(r != null && settings.getSimilarityCheckSetting() != null) {
            r = settings.getSimilarityCheckSetting().checkMessage(sender, r);
        }
        return r;
    }

    public boolean checkCooldown(@NotNull PlayerSender sender) {
        if(cooldown == null) return true;
        if(cooldown.getSkipPermission() != null && sender.hasPermission(cooldown.getSkipPermission())) return true;

        long lg = sender.getCooldowns().getCooldown(name, cooldown.getTime());
        if(lg > 0) {
            CooldownUtils.sendCooldown(sender, lg, cooldown.getTime(), cooldown.getMessages(), cooldown.getFormat());
            return false;
        }

        return true;
    }

    public void applyCooldown(@NotNull PlayerSender sender) {
        if(cooldown == null) return;
        if(cooldown.getSkipPermission() != null && sender.hasPermission(cooldown.getSkipPermission())) return;

        sender.getCooldowns().addCooldown(name, System.currentTimeMillis());
    }

    public @NotNull String toFormat(@NotNull PlayerSender sender, @NotNull String message) {
        String r = message;
        for(Map.Entry<String, String> e : replacesMap.entrySet()) {
            r = r.replace(e.getKey(), e.getValue());
        }

        if(settings.getMessageCapitalize() != null) {
            r = settings.getMessageCapitalize().checkMessage(sender, r);
        }
        if(settings.getMessageDotInsertion() != null) {
            r = settings.getMessageDotInsertion().checkMessage(sender, r != null ? r : message);
        }
        if(settings.getMessageColorize() != null) {
            r = settings.getMessageColorize().checkMessage(sender, r != null ? r : message);
        }

        return r != null ? r : message;
    }

    public static @Nullable Chat get(@NotNull String name) {
        return map.get(name);
    }

    public void clear() {
        map.remove(name);
        list.remove(this);
    }

    public static void sort() {
        list.sort((o1, o2) -> {
            String s1 = o1.getChatSettings().getSymbol();
            String s2 = o2.getChatSettings().getSymbol();

            if(s1 == null && s2 == null) return 0;

            if(s1 == null) {
                return 1;
            } else if(s2 == null) {
                return -1;
            } else {
                return Integer.compare(s2.length(), s1.length());
            }
        });
    }

    public static Collection<Chat> chats() {
        return list;
    }

    public static Set<Map.Entry<String, Chat>> all() {
        return map.entrySet();
    }

    public static void clearAll() {
        map.clear();
    }
}
