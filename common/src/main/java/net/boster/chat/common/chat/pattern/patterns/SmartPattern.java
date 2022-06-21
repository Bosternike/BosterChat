package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.pattern.MessagePattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SmartPattern implements MessagePattern {

    @Getter @NotNull private final String initialPattern;
    @Getter @NotNull private final List<MessagePattern> sequence;

    @Override
    public @NotNull String process(@NotNull String s) {
        int t = 0;
        String r = "";

        for(int i = 0; i < s.length(); i++) {
            if(t >= sequence.size()) {
                t = 0;
            }

            String c = String.valueOf(s.charAt(i));
            if(c.equals(" ")) {
                r += " ";
            } else {
                r += sequence.get(t).process(c);
                t++;
            }
        }

        return r;
    }
}
