package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class RandomCharPattern implements MessagePattern {

    @Getter @NotNull private final String initialPattern;
    @Getter @NotNull private final List<MessagePattern> list;

    @Override
    public @NotNull String process(@NotNull String s) {
        String r = "";
        for(int i = 0; i < s.length(); i++) {
            String c = String.valueOf(s.charAt(i));
            r += c.equals(" ") ? " " : list.get(ThreadLocalRandom.current().nextInt(list.size())).process(c) + ChatColor.RESET;
        }

        return r;
    }
}
