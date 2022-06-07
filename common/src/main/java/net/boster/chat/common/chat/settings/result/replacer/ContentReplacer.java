package net.boster.chat.common.chat.settings.result.replacer;

import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContentReplacer implements BCReplacer {

    @Override
    public @NotNull String replace(@NotNull String message, @NotNull List<String> list, @NotNull String replacer) {
        String r = "";
        for(String s : message.split(" ")) {
            r += checkString(s, list, replacer);
        }
        return r;
    }

    private String checkString(String s, List<String> list, @NotNull String replacer) {
        String r = "";
        for(String l : list) {
            r += TextUtils.replace(s, l, replacer);
        }
        r = s;
        return r;
    }
}
