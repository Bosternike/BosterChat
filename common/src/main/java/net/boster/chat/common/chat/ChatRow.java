package net.boster.chat.common.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChatRow {

    @Getter @NotNull private final String id;
    @Getter @Setter @NotNull private List<ChatComponent> components;

    public ChatRow(@NotNull ConfigurationSection section, @NotNull String name) {
        this.id = name;
        this.components = new ArrayList<>();
        for(String s : section.getKeys()) {
            ConfigurationSection r = section.getSection(s);
            if(r != null) {
                if(r.getString("text") != null) {
                    components.add(new ChatComponent(r));
                }
            }
        }
    }
}
