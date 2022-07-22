package net.boster.chat.common;

import com.google.common.collect.ImmutableList;
import net.boster.chat.common.chat.Chat;
import net.boster.chat.common.chat.ChatRow;
import net.boster.chat.common.data.setter.DataSetter;
import net.boster.chat.common.events.DirectMessageEvent;
import net.boster.chat.common.handler.ChatHandler;
import net.boster.chat.common.handler.DirectMessageHandler;
import net.boster.chat.common.placeholders.PlaceholdersManager;
import net.boster.chat.common.placeholders.PlaceholdersRequest;
import net.boster.chat.common.provider.BosterChatProvider;
import net.boster.chat.common.provider.ChatColorProvider;
import net.boster.chat.common.provider.PlaceholderProvider;
import net.boster.chat.common.sender.CommandSender;
import net.boster.chat.common.sender.PlayerSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
        BosterChatProvider.loadDirectSettings();
        BosterChatProvider.loadChats();
    }

    public static <T> void registerPlaceholders(@NotNull PlaceholderProvider<T> provider) {
        PlaceholdersManager.addProvider(provider);
    }

    public static <T> void registerPlaceholders(@NotNull Class<T> clazz, @NotNull Function<PlaceholdersRequest<T>, @NotNull String> function) {
        PlaceholdersManager.addProvider(clazz, function);
    }

    public static @Nullable DirectMessageHandler getDirectMessageHandler() {
        return ChatHandler.getDirectMessageHandler();
    }

    public static void setDirectMessageHandler(@NotNull DirectMessageHandler handler) {
        ChatHandler.setDirectMessageHandler(handler);
    }

    public static Chat getChat(@NotNull String s) {
        return Chat.get(s);
    }

    public static void sendFromConsole(@NotNull String message, @NotNull PlayerSender receiver) {
        if(ChatHandler.getDirectMessageHandler() == null) return;

        ChatHandler.directMessageEvent(new DirectMessageEvent(get().getConsole(), receiver, message, false));
    }
}
