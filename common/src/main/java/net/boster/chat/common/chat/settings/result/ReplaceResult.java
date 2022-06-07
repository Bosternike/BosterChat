package net.boster.chat.common.chat.settings.result;

import lombok.Getter;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReplaceResult extends RSResult {

    @Getter @NotNull private final String replacement;

    private final boolean replaceWords;

    public ReplaceResult(@NotNull List<String> list, @NotNull String replacement, boolean replaceWords) {
        super(list);
        this.replacement = replacement;
        this.replaceWords = replaceWords;
    }

    @Override
    public @Nullable String result(@NotNull PlayerSender sender, @NotNull String message) {
        String r;

        if(replaceWords) {
            r = "";
            String a = message.startsWith(" ") ? " " : "";
            for(String s : message.split(" ")) {
                r += a + checkString(message, list, replacement);
                a = " ";
            }
        } else {
            r = message;
            for(String s : list) {
                r = r.replaceAll("(?i)" + s, replacement);
            }
        }

        return r;
    }

    private String checkString(String s, List<String> list, @NotNull String replacer) {
        for(String l : list) {
            if(s.equalsIgnoreCase(l)) {
                return replacer;
            }
        }
        return s;
    }
}
