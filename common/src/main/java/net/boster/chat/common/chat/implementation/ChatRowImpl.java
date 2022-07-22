package net.boster.chat.common.chat.implementation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChatRowImpl implements ChatRow {

    @Getter @NotNull private final String id;
    @Getter @Setter @NotNull private List<ChatComponent> components;

    public ChatRowImpl(@NotNull ConfigurationSection section, @NotNull String name) {
        this.id = name;
        this.components = new ArrayList<>();
        for(String s : section.getKeys()) {
            ConfigurationSection r = section.getSection(s);
            if(r != null && r.getString("text") != null) {
                components.add(new ChatComponent(r));
            }
        }
    }
}
