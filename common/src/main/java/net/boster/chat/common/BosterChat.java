package net.boster.chat.common;

import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.data.setter.DataSetter;
import net.boster.chat.common.placeholders.PlaceholdersManager;
import net.boster.chat.common.placeholders.PlaceholdersRequest;
import net.boster.chat.common.provider.BosterChatProvider;
import net.boster.chat.common.provider.ChatColorProvider;
import net.boster.chat.common.provider.PlaceholderProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BosterChat {

    public static @NotNull BosterChatPlugin get() {
        return BosterChatProvider.getProvider();
    }

    public static @NotNull DataSetter getDataSetter() {
        return BosterChatProvider.getDataSetter();
    }

    public static void reload() {
        get().getConfigFile().load();
        get().getChatsFile().load();

        BosterChatProvider.checkConfig();
        ChatColorProvider.load();
        BosterChatProvider.loadDataSetter();
        BosterChatProvider.loadCooldowns();
        BosterChatProvider.loadChats();
    }

    public static <T> void registerPlaceholders(@NotNull PlaceholderProvider<T> provider) {
        PlaceholdersManager.addProvider(provider);
    }

    public static <T> void registerPlaceholders(@NotNull Class<T> clazz, @NotNull Function<PlaceholdersRequest<T>, @NotNull String> function) {
        PlaceholdersManager.addProvider(clazz, function);
    }

    public static Chat getChat(@NotNull String s) {
        return Chat.get(s);
    }
}
