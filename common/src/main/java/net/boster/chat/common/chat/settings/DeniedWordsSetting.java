package net.boster.chat.common.chat.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DeniedWordsSetting implements Setting {

    @Getter @Setter protected boolean enabled;

    @Getter @Setter @NotNull private OverridePermissionSetting overridePermissionSetting;
    @Getter @Setter @NotNull private List<String> list;
    @Getter @Setter @Nullable private ResultedSetting resultedSetting;

    public DeniedWordsSetting(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("Enabled", false);

        ConfigurationSection op = section.getSection("OverridePermission");
        if(op != null) {
            this.overridePermissionSetting = new OverridePermissionSetting(op);
        } else {
            this.overridePermissionSetting = OverridePermissionSetting.empty();
        }

        this.list = section.getStringList("list");

        ConfigurationSection rs = section.getSection("Result");
        if(rs != null) {
            this.resultedSetting = new ResultedSetting(rs, list, true);
        }
    }

    @Override
    public @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message) {
        if(!enabled) return message;

        if(overridePermissionSetting.isEnabled()) {
            if(overridePermissionSetting.getPermission() != null && sender.hasPermission(overridePermissionSetting.getPermission())) {
                return message;
            }
        }

        if(resultedSetting != null) {
            return resultedSetting.getResult().result(sender, message);
        }

        return message;
    }
}
