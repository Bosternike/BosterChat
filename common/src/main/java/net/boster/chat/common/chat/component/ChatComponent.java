package net.boster.chat.common.chat.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.button.ButtonBoundActions;
import net.boster.chat.common.config.ConfigurationSection;
import net.md_5.bungee.api.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ChatComponent implements Cloneable {

    @Getter @NotNull private String text;
    @Getter @Nullable private String hover;
    @Getter @Nullable private ClickEvent.Action actionType;
    @Getter @Nullable private String actionString;

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

    public ChatComponent() {
        this("");
    }

    public ChatComponent attachButton(@NotNull ButtonBoundActions.CompletableAction action) {
        setActionType(ClickEvent.Action.RUN_COMMAND);
        setActionString(action.getCompleteCommand());
        return this;
    }

    public ChatComponent setText(@NotNull String s) {
        text = s;
        return this;
    }

    public ChatComponent setHover(@Nullable String s) {
        hover = s;
        return this;
    }

    public ChatComponent setActionType(@Nullable ClickEvent.Action action) {
        actionType = action;
        return this;
    }

    public ChatComponent setActionString(@Nullable String s) {
        actionString = s;
        return this;
    }

    @Override
    public ChatComponent clone() {
        try {
            return (ChatComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
