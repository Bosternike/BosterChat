package net.boster.chat.common.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleButtonBoundActions implements ButtonBoundActions {

    private int lastId = 0;
    private final Map<Integer, CompletableAction> map = new LinkedHashMap<>();

    @Override
    public int lastId() {
        return lastId;
    }

    @Override
    public @NotNull CompletableAction addAction(@NotNull Action action) {
        lastId++;
        CompletableAction a = new CompletableAction("/button-cmd-completer " + lastId, this, lastId, action);
        map.put(lastId, a);
        return a;
    }

    @Override
    public @Nullable CompletableAction get(int id) {
        return map.get(id);
    }

    @Override
    public void remove(@NotNull CompletableAction action) {
        remove(action.getId());
    }

    @Override
    public void remove(int id) {
        map.remove(id);
    }
}
