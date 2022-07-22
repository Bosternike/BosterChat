package net.boster.chat.common.chat.message;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.chat.implementation.SimpleChatRow;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageRowBuilder {

    @Getter @Setter @NotNull private List<ChatComponent> components = new ArrayList<>();

    public MessageRowBuilder append(@NotNull ChatComponent component) {
        components.add(component);
        return this;
    }

    public MessageRowBuilder appendComponent(@NotNull String text) {
        components.add(new ChatComponent(text));
        return this;
    }

    public MessageRowBuilder appendComponent(@NotNull String text, @Nullable String hover, @Nullable ClickEvent.Action actionType, @Nullable String actionString) {
        components.add(new ChatComponent(text).setHover(hover).setActionType(actionType).setActionString(actionString));
        return this;
    }

    public @NotNull ChatRow createRow() {
        return new SimpleChatRow(new ArrayList<>(components));
    }

    public @NotNull TextComponent[] createTextComponentArray() {
        return createTextComponent().toArray(new TextComponent[]{});
    }

    public @NotNull List<TextComponent> createTextComponent() {
        return toTextComponent(components);
    }

    public static @NotNull List<TextComponent> toTextComponent(@NotNull List<ChatComponent> components) {
        List<TextComponent> list = new ArrayList<>();

        for(ChatComponent c : components) {
            BaseComponent[] tc = TextComponent.fromLegacyText(c.getText());

            HoverEvent hover = null;
            if(c.getHover() != null) {
                hover = BosterChat.get().createHover(c.getHover());
            }

            ClickEvent click = null;
            if(c.getActionType() != null && c.getActionString() != null) {
                click = new ClickEvent(c.getActionType(), c.getActionString());
            }

            for(int i = 0; i < tc.length; i++) {
                if(hover != null) {
                    tc[i].setHoverEvent(hover);
                }
                if(click != null) {
                    tc[i].setClickEvent(click);
                }
            }

            for(BaseComponent t : tc) {
                list.add((TextComponent) t);
            }
        }

        return list;
    }
}
