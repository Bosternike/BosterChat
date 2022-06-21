package net.boster.chat.common.chat.pattern.patterns;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.boster.chat.common.chat.pattern.MessagePattern;
import net.boster.chat.common.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class RGBPattern implements MessagePattern {

    @Getter @NotNull private final String initialPattern;

    @Getter @NotNull private final Color start;
    @Getter @NotNull private final Color end;

    public RGBPattern() {
        this(Color.YELLOW, Color.MAGENTA);
    }

    public RGBPattern(@Nullable Color start, @Nullable Color end) {
        this.start = start != null ? start : Color.YELLOW;
        this.end = end != null ? end : Color.MAGENTA;

        this.initialPattern = "rgb:" + this.start.getRed() + "," + this.start.getGreen() + "," + this.start.getBlue() + ";" +
                this.end.getRed() + "," + this.end.getGreen() + "," + this.end.getBlue();
    }

    @Override
    public @NotNull String process(@NotNull String s) {
        return ChatUtils.color(s, start, end);
    }
}
