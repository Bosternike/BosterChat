package net.boster.chat.common.chat.settings.result;

import lombok.Getter;
import net.boster.chat.common.chat.settings.result.replacer.BCReplacer;
import net.boster.chat.common.chat.settings.result.replacer.ContentReplacer;
import net.boster.chat.common.chat.settings.result.replacer.WordsReplacer;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReplaceCharsResult extends RSResult {

    @Getter @NotNull private final String replacer;

    private final BCReplacer cr;

    public ReplaceCharsResult(@NotNull List<String> list, @NotNull String replacer, boolean replaceWords) {
        super(list);
        this.replacer = replacer;
        if(replaceWords) {
            this.cr = new WordsReplacer();
        } else {
            this.cr = new ContentReplacer();
        }
    }

    @Override
    public @Nullable String result(@NotNull PlayerSender sender, @NotNull String message) {
        if(message.contains(" ")) {
            return cr.replace(message, list, replacer);
        } else {
            for(String s : list) {
                if(message.equalsIgnoreCase(s)) {
                    String r = "";
                    for(int i = 0; i < message.length(); i++) {
                        r += replacer;
                    }
                    return r;
                }
            }
        }

        return message;
    }
}
