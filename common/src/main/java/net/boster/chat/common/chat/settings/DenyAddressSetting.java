package net.boster.chat.common.chat.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.settings.result.MessageResult;
import net.boster.chat.common.chat.settings.result.RSResult;
import net.boster.chat.common.chat.settings.result.ReplaceCharsResult;
import net.boster.chat.common.chat.settings.result.ReplaceResult;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DenyAddressSetting implements Setting {

    @Getter @Setter protected boolean enabled;

    @Getter @Setter @NotNull protected OverridePermissionSetting overridePermissionSetting;
    @Getter @Setter @NotNull protected List<String> list;
    @Getter @Setter @Nullable protected ResultedSetting resultedSetting;

    public DenyAddressSetting(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("Enabled", false);

        ConfigurationSection op = section.getSection("OverridePermission");
        if(op != null) {
            this.overridePermissionSetting = new OverridePermissionSetting(op);
        } else {
            this.overridePermissionSetting = OverridePermissionSetting.empty();
        }

        this.list = section.getStringList("AllowedIPs");

        ConfigurationSection rs = section.getSection("Result");
        if(rs != null) {
            this.resultedSetting = new ResultedSetting(rs, list, false);
        }
    }

    public @Nullable String checkIP(@NotNull PlayerSender sender, @NotNull String s) {
        if(resultedSetting == null) return s;

        RSResult r = resultedSetting.getResult();

        if(r instanceof ReplaceResult) {
            String a = ((ReplaceResult) r).getReplacement();
            s = s.replaceAll(TextUtils.IP_REGEX, a);
            s = s.replaceAll(TextUtils.DOMAIN_REGEX, a);
        } else if(r instanceof ReplaceCharsResult) {
            String a = "<unsupported>";
            s = s.replaceAll(TextUtils.IP_REGEX, a);
            s = s.replaceAll(TextUtils.DOMAIN_REGEX, a);
        } else if(r instanceof MessageResult) {
            String m = s;
            m = m.replaceAll(TextUtils.IP_REGEX, "");
            m = m.replaceAll(TextUtils.DOMAIN_REGEX, "");
            if(!m.equals(s)) {
                for(String msg : ((MessageResult) r).getMessage()) {
                    sender.sendMessage(ChatUtils.toColor(msg));
                }

                return null;
            }
        }

        return s;
    }

    @Override
    public @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message) {
        if(!enabled) return message;

        if(overridePermissionSetting.isEnabled()) {
            if(overridePermissionSetting.getPermission() != null && sender.hasPermission(overridePermissionSetting.getPermission())) {
                return message;
            }
        }

        return checkIP(sender, message);
    }
}
