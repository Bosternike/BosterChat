package net.boster.chat.common.handler;

import net.boster.chat.common.events.ChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class ChatHandler {

    private static final List<ChatHandler> handlerList = new LinkedList<>();

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
