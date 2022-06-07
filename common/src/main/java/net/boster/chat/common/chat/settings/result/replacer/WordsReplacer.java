package net.boster.chat.common.chat.settings.result.replacer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WordsReplacer implements BCReplacer {

    @Override
    public @NotNull String replace(@NotNull String message, @NotNull List<String> list, @NotNull String replacer) {
        String r = "";
        String a = message.startsWith(" ") ? " " : "";
        for(String s : message.split(" ")) {
            r += a + checkString(s, list, replacer);
            a = " ";
        }
        return r;
    }

    private String checkString(String s, List<String> list, @NotNull String replacer) {
        String r = "";
        for(String l : list) {
            if(s.equalsIgnoreCase(l)) {
                for(int i = 0; i < s.length(); i++) {
                    r += replacer;
                }
                return r;
            }
        }
        r = s;
        return r;
    }
}
