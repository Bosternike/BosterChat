package net.boster.chat.common.chat.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.settings.result.MessageResult;
import net.boster.chat.common.chat.settings.result.RSResult;
import net.boster.chat.common.chat.settings.result.ReplaceCharsResult;
import net.boster.chat.common.chat.settings.result.ReplaceResult;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
public class ResultedSetting {

    @Getter @Setter @NotNull private RSResult result;

    public ResultedSetting(@NotNull ConfigurationSection section, @NotNull List<String> list, boolean checkWords) {
        if(section.getString("replaceChars") != null) {
            this.result = new ReplaceCharsResult(list, section.getString("replaceChars"), checkWords);
        } else if(section.getString("replace") != null) {
            this.result = new ReplaceResult(list, section.getString("replace"), checkWords);
        } else {
            this.result = new MessageResult(list, section.getStringList("message"), checkWords);
        }
    }
}
