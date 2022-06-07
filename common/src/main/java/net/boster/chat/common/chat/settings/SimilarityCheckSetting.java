package net.boster.chat.common.chat.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.chat.common.config.ConfigurationSection;
import net.boster.chat.common.sender.PlayerSender;
import net.boster.chat.common.utils.ChatUtils;
import net.boster.chat.common.utils.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SimilarityCheckSetting implements Setting {

    @Getter @Setter protected boolean enabled;

    @Getter @Setter protected double similarity;
    @Getter @Setter @NotNull protected List<String> message;

    public SimilarityCheckSetting(@NotNull ConfigurationSection section) {
        this.enabled = section.getBoolean("Enabled", false);
        this.similarity = Math.max(0, Math.min(100, section.getDouble("Similarity", 80)));
        this.message = section.getStringList("Message");
    }

    public boolean isSimilar(@NotNull String message, @NotNull String lastMessage) {
        String s = TextUtils.stripMessage(message);

        return TextUtils.similarity(s, lastMessage) > similarity;
    }

    public boolean isSimilar(@NotNull String message, @NotNull Collection<String> lastMessages) {
        String s = TextUtils.stripMessage(message);

        for(String l : lastMessages) {
            if(TextUtils.similarity(s, l) > similarity) {
                return true;
            }
        }

        return false;
    }

    @Override
    public @Nullable String checkMessage(@NotNull PlayerSender sender, @NotNull String message) {
        if(!enabled) return message;

        if(!isSimilar(message, sender.getRecentMessages())) return message;

        for(String msg : this.message) {
            sender.sendMessage(ChatUtils.toColor(msg));
        }
        return null;
    }
}
