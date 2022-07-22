package net.boster.chat.common.chat.direct;

import lombok.*;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
public class DirectSession {

    @Getter @NotNull private final PlayerSender player;
    @Getter @Setter @NotNull private List<DirectMessage> sentMessages = new ArrayList<>();
    @Getter @Setter @NotNull private List<DirectMessage> receivedMessages = new ArrayList<>();
    @Getter @Setter @NotNull private List<DirectMessage> notRepliedMessages = new ArrayList<>();
}
