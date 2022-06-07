package net.boster.chat.common.cooldowns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
public class CooldownMessages {

    @Getter @Setter @Nullable private String message;
    @Getter @Setter @Nullable private String title;
    @Getter @Setter @Nullable private String subTitle;
    @Getter @Setter @Nullable private String actionbar;

    public static CooldownMessages load(@Nullable ConfigurationSection section) {
        if(section == null) {
            return CooldownUtils.cooldownMessages;
        }

        CooldownMessages c = new CooldownMessages();
        if(section.getBoolean("Message.Enabled", false)) {
            c.message = section.getString("Message.text");
        }
        if(section.getBoolean("Title.Enabled", false)) {
            c.title = section.getString("Title.text");
        }
        if(section.getBoolean("SubTitle.Enabled", false)) {
            c.subTitle = section.getString("SubTitle.text");
        }
        if(section.getBoolean("Actionbar.Enabled", false)) {
            c.actionbar = section.getString("Actionbar.text");
        }

        return c;
    }
}
