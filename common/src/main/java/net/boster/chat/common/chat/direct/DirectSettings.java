package net.boster.chat.common.chat.direct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
public class DirectSettings {

    @Getter @NotNull private final PlayerSender player;

    @Getter @Setter private boolean enabled = true;
    @Getter @Setter private List<String> ignoring = new ArrayList<>();
    @Getter @Setter private Map<String, Boolean> enabledMap = new HashMap<>();

    public boolean isIgnored(@NotNull String user) {
        return ignoring.contains(user);
    }

    public void ignore(@NotNull String user) {
        ignoring.add(user);
    }

    public void unIgnore(@NotNull String user) {
        ignoring.remove(user);
    }
}
