package net.boster.chat.common.chat;

import lombok.*;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ChatSettings {

    /**
     * Set to null to disable log.
     * Variables: %date%, %player%%, %message%, %chat%
     */
    @Getter @Setter @Nullable private String logFormat;
    @Getter @Setter private boolean logToConsole;
    @Getter @Setter @Nullable private String symbol;
    @Getter @Setter private double showDistance = -1;
    @Getter @Setter @NotNull private List<String> worlds;
    @Getter @Setter @NotNull private List<String> servers;
    @Getter @Setter @Nullable private String seeMessagesPermission;
    @Getter @Setter @Nullable private Permission permission;
    @Getter @Setter @Nullable private String messagePatternPermission;

    public ChatSettings(@NotNull ConfigurationSection section) {
        if(section.getBoolean("Log.Enabled", false)) {
            this.logFormat = section.getString("Log.Format");
        }
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
            this.seeMessagesPermission = sc.getString("perm");
        }

        ConfigurationSection smp = section.getSection("MessagePatterPermissionRequirement");
        if(smp != null && smp.getBoolean("Enabled", false)) {
            this.messagePatternPermission = smp.getString("Permission");
        }
    }

    public boolean canUseMessagePattern(@NotNull PlayerSender sender) {
        return messagePatternPermission == null || sender.hasPermission(messagePatternPermission);
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
