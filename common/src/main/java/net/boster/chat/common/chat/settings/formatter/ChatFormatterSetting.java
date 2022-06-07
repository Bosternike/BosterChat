package net.boster.chat.common.chat.settings.formatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.settings.Setting;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ChatFormatterSetting implements Setting {

    @Getter @Setter protected boolean enabled;

    @Getter @Setter @Nullable protected String permission;

    public ChatFormatterSetting(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("Enabled");

        ConfigurationSection perm = section.getSection("PermissionRequirement");
        if(perm != null && perm.getBoolean("Enabled", false)) {
            this.permission = perm.getString("Permission");
        }
    }

    public boolean hasPermission(@NotNull PlayerSender sender) {
        return permission == null || sender.hasPermission(permission);
    }
}
