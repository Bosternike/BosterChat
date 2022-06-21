package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.chat.common.chat.pattern.MessagePattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class RandomPattern implements MessagePattern {

    @Getter @NotNull private final String initialPattern;
    @Getter @NotNull private final List<MessagePattern> list;

    @Override
    public @NotNull String process(@NotNull String s) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size())).process(s);
    }
}
