package net.boster.chat.bukkit;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.bukkit.utils.VersionManager;
import net.boster.chat.common.BosterChat;
import net.boster.chat.common.button.ButtonBoundActions;
import net.boster.chat.common.button.SimpleButtonBoundActions;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.message.ChatMessage;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.chat.component.ChatComponent;
import net.boster.chat.common.chat.message.ChatMessageBuilder;
import net.boster.chat.common.chat.placeholders.ChatMessagePlaceholder;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsoleSender implements CommandSender {

    @Getter @Setter @NotNull private ButtonBoundActions buttonBoundActions = new SimpleButtonBoundActions();

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public @NotNull ChatMessage sendTo(@NotNull Chat chat, @NotNull String message, @NotNull Collection<? extends PlayerSender> players, boolean b,
                                       @Nullable List<ChatMessagePlaceholder> placeholders, @NotNull ChatRow... rows) {

        ChatMessageBuilder mb = new ChatMessageBuilder();
        mb.setSender(this);
        mb.setChat(chat);
        mb.getReceivers().addAll(players);
        mb.getBox().setInitialMessage(message);

        for(ChatRow row : rows) {
            StringBuilder sb = new StringBuilder();
            List<TextComponent> list = new ArrayList<>();

            for(ChatComponent cc : row.getComponents()) {
                String s = toPlaceholders(cc.getText(), message, placeholders);
                sb.append(s);
                BaseComponent[] tc = TextComponent.fromLegacyText(s);
                HoverEvent hover = null;
                if(cc.getHover() != null) {
                    hover = VersionManager.buildHover(toPlaceholders(cc.getHover(), message, placeholders));
                }
                ClickEvent click = null;
                if(cc.getActionType() != null && cc.getActionString() != null) {
                    click = new ClickEvent(cc.getActionType(), toPlaceholders(cc.getActionString(), message, placeholders));
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

            Bukkit.getConsoleSender().sendMessage(sb.toString());
            TextComponent[] components = list.toArray(new TextComponent[]{});
            for(PlayerSender p : players) {
                p.sendMessage(components);
            }

            mb.getBox().addRow(sb.toString(), components);
        }

        return mb.toMessage();
    }

    public String toPlaceholders(@NotNull String s, @NotNull String message, @Nullable List<ChatMessagePlaceholder> placeholders) {
        String r = ChatUtils.toColor(s.replace("%display_name%", getName())
                .replace("%name%", getName())
                .replace("%world%", "none")
                .replace("%food%", "20")
                .replace("%exp%", "0")
                .replace("%prefix%", "")
                .replace("%suffix%", "")
                .replace("%clan%", BosterChat.get().getPlaceholders().NO_CLAN())
                .replace("%%towny_town%", BosterChat.get().getPlaceholders().NO_TOWN())
                .replace("%towny_nation%", BosterChat.get().getPlaceholders().NO_NATION()));
        r = r.replace("%message%", message).replace("%colored_message%", ChatUtils.toColor(message));

        if(placeholders != null) {
            for(ChatMessagePlaceholder cmp : placeholders) {
                r = r.replace(cmp.getPlaceholder(), cmp.getReplacement());
            }
        }

        return r;
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return true;
    }
}
