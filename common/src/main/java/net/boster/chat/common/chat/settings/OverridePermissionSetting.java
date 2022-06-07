package net.boster.chat.common.chat.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
public class OverridePermissionSetting {

    @Getter @Setter protected boolean enabled;
    @Getter @Setter @Nullable protected String permission;

    public OverridePermissionSetting(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("Enabled", false);
        this.permission = section.getString("Permission");
    }

    public static OverridePermissionSetting empty() {
        return new OverridePermissionSetting(false, null);
    }
}
