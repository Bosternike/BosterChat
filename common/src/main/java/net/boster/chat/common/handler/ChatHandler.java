package net.boster.chat.common.handler;

import lombok.Getter;
import lombok.Setter;
import net.boster.chat.common.events.ChatEvent;
import net.boster.chat.common.events.DirectMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public abstract class ChatHandler {

    private static final List<ChatHandler> handlerList = new LinkedList<>();
    @Getter @Setter @Nullable private static DirectMessageHandler directMessageHandler;

    public static void registerHandler(@NotNull ChatHandler handler) {
        handlerList.add(handler);
    }

    public static void unregisterHandler(@NotNull ChatHandler handler) {
        handlerList.remove(handler);
    }

    public static void callEvent(@NotNull ChatEvent event) {
        for(int i = 0; i < handlerList.size(); i++) {
            ChatHandler h = handlerList.get(i);
            if(event.isCancelled() && !h.ignoreCancelled()) continue;
            if(event.isContinued() && !h.ignoreContinued()) continue;

            h.onEvent(event);
        }
    }

    public static void directMessageEvent(@NotNull DirectMessageEvent event) {
        if(directMessageHandler == null) return;

        directMessageHandler.onEvent(event);
    }

    public abstract boolean ignoreCancelled();
    public abstract boolean ignoreContinued();
    public abstract void onEvent(@NotNull ChatEvent event);

    public final void register() {
        registerHandler(this);
    }

    public final void unregister() {
        unregisterHandler(this);
    }
}
