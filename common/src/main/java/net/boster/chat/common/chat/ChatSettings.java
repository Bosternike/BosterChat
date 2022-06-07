package net.boster.chat.common.chat;

import lombok.*;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ChatSettings {

    @Getter @Setter private boolean logToConsole;
    @Getter @Setter @Nullable private String symbol;
    @Getter @Setter private double showDistance = -1;
    @Getter @Setter @NotNull private List<String> worlds;
    @Getter @Setter @NotNull private List<String> servers;
    @Getter @Setter @Nullable private String seeMessagesPermission;
    @Getter @Setter @Nullable private Permission permission;

    public ChatSettings(@NotNull ConfigurationSection section) {
        this.logToConsole = section.getBoolean("LogToConsole", false);
        this.symbol = section.getString("symbol");
        this.showDistance = section.getDouble("showDistance", -1);
        this.worlds = section.getStringList("worlds");
        this.servers = section.getStringList("servers");

        ConfigurationSection p = section.getSection("Permission");
        if(p != null && p.getBoolean("Enabled", false)) {
            this.permission = new Permission(p);
        }

        ConfigurationSection sc = section.getSection("SeeMessagesPermission");
        if(sc != null && sc.getBoolean("Enabled", false)) {
            this.seeMessagesPermission = sc.getString("SeeMessagesPermission");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class Permission {
        @Getter @Setter @NotNull private String permission;
        @Getter @Setter @Nullable private String message;

        public Permission(@NotNull ConfigurationSection section) {
            this.permission = section.getString("perm");
            this.message = section.getString("message");
        }
    }
}
