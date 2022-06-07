package net.boster.chat.common.chat.implementation;

import net.boster.chat.common.chat.placeholders.ChatPlaceholders;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class ChatPlaceholdersImpl implements ChatPlaceholders {

    private final String noClan;
    private final String noTown;
    private final String noNation;

    public ChatPlaceholdersImpl(@NotNull ConfigurationSection section) {
        this.noClan = section.getString("NoClan", "");
        this.noTown = section.getString("NoTown", "");
        this.noNation = section.getString("NoNation", "");
    }

    @Override
    public @NotNull String NO_CLAN() {
        return noClan;
    }

    @Override
    public @NotNull String NO_TOWN() {
        return noTown;
    }

    @Override
    public @NotNull String NO_NATION() {
        return noNation;
    }
}
