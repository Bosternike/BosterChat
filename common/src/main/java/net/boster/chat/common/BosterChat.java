package net.boster.chat.common;

import net.boster.chat.common.placeholders.PlaceholdersManager;
import net.boster.chat.common.provider.BosterChatProvider;
import net.boster.chat.common.provider.PlaceholderProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BosterChat {

    public static BosterChatPlugin get() {
        return BosterChatProvider.getProvider();
    }

    public static void reload() {
        get().getConfigFile().load();
        get().getChatsFile().load();

        BosterChatProvider.checkConfig();
        BosterChatProvider.loadCooldowns();
        BosterChatProvider.loadChats();
    }

    public static <T> void registerPlaceholders(@NotNull PlaceholderProvider<T> provider) {
        PlaceholdersManager.addProvider(provider);
    }

    public static <T> void registerPlaceholders(@NotNull Class<T> clazz, @NotNull Function<T, @NotNull String> function) {
        PlaceholdersManager.addProvider(clazz, function);
    }
}
