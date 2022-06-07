package net.boster.chat.common.chat.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import net.md_5.bungee.api.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ChatComponent {

    @Getter @Setter @NotNull private String text;
    @Getter @Setter @Nullable private String hover;
    @Getter @Setter @Nullable private ClickEvent.Action actionType;
    @Getter @Setter @Nullable private String actionString;

    public ChatComponent(@NotNull ConfigurationSection section) {
        this.text = section.getString("text");
        this.hover = section.getString("hover");
        ConfigurationSection a = section.getSection("action");
        if(a != null) {
            try {
                this.actionType = ClickEvent.Action.valueOf(a.getString("type"));
            } catch (Exception ignored) {}
            this.actionString = a.getString("string");
        }
    }
}
