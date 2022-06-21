package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.pattern.MessagePattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SplitPattern implements MessagePattern {

    @Getter @NotNull private final String initialPattern;
    @Getter @NotNull private final List<MessagePattern> patterns;

    @Override
    public @NotNull String process(@NotNull String s) {
        if(s.length() >= patterns.size()) {
            return process(s, s.length() / patterns.size());
        } else {
            return process(s, s.length());
        }
    }

    private String process(String s, int d) {
        String r = "";
        int o = 0;

        for(int i = 0; i <= d; i += d) {
            r += patterns.get(o).process(s.substring(i, i + d));
            o++;
        }

        return r;
    }
}
