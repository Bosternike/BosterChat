package net.boster.chat.common.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.log.LogType;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ChatColorProvider {

    @Getter @Setter @NotNull private static String disableArgument = "off";
    @Getter @Setter @NotNull private static Map<String, MessagePattern> messagePatterns = new HashMap<>();
    @Getter @Setter @NotNull private static Map<String, LimitedPermission> limitedPermissions = new HashMap<>();

    public static void load() {
        messagePatterns.clear();
        limitedPermissions.clear();

        ConfigurationSection c = BosterChat.get().config().getSection("ChatColor");
        if(!c.getBoolean("Enabled", false)) return;

        disableArgument = c.getString("DisableArgument", "off");

        ConfigurationSection patterns = c.getSection("Patterns");
        if(patterns != null) {
            for(String p : patterns.getKeys()) {
                MessagePattern mp = ChatUtils.getPatternReader().read(patterns.getString(p));
                if(mp != null) {
                    messagePatterns.put(p, mp);
                }
            }
        }

        ConfigurationSection lp = c.getSection("limitedPermissions");
        if(lp != null) {
            for(String l : lp.getKeys()) {
                ConfigurationSection perm = lp.getSection(l);
                if(perm != null) {
                    loadPermission(l, perm);
                }
            }
        }
    }

    private static void loadPermission(@NotNull String s, @NotNull ConfigurationSection section) {
        List<String> colorCodes = new ArrayList<>();
        Map<String, MessagePattern> patterns = new HashMap<>();
        Map<String, MessagePattern> others = new HashMap<>();
        try {
            if(section.getString("ColorCodes").equalsIgnoreCase("all")) {
                colorCodes.addAll(Arrays.stream(ChatUtils.ALL_CODES).map(String::valueOf).collect(Collectors.toList()));
            } else {
                colorCodes.addAll(Arrays.asList(section.getString("ColorCodes").replace(" ", "").split(",")));
            }
        } catch (Exception ignored) {}

        for(String p : section.getStringList("Patterns")) {
            MessagePattern mp = messagePatterns.get(p);
            if(mp == null) {
                BosterChat.get().log("&7Could not define chat color pattern \"&c" + p + "&7\". Requested limited permissions (Patterns): &9" + s, LogType.WARNING);
                continue;
            }

            patterns.put(p, mp);
        }

        for(String p : section.getStringList("Others")) {
            try {
                MessagePattern mp = ChatUtils.getPatternReader().read(p);
                others.put(p, mp);
            } catch (Exception e) {
                BosterChat.get().log("&7Could not read chat color pattern \"&c" + p + "&7\". Requested limited permissions (Others): &9" + s, LogType.WARNING);
            }
        }

        limitedPermissions.put(s, new LimitedPermission(s, colorCodes, patterns, others));
    }

    @RequiredArgsConstructor
    public static class LimitedPermission {
        @Getter @NotNull private final String name;
        @Getter @NotNull private final List<String> colorCodes;
        @Getter @NotNull private final Map<String, MessagePattern> patterns;
        @Getter @NotNull private final Map<String, MessagePattern> others;
    }
}
