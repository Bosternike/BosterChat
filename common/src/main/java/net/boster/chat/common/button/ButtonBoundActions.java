package net.boster.chat.common.button;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ButtonBoundActions {

    int lastId();

    @NotNull CompletableAction addAction(@NotNull Action action);

    @Nullable CompletableAction get(int id);

    void remove(@NotNull CompletableAction action);
    void remove(int id);

    @RequiredArgsConstructor
    class CompletableAction {

        @Getter @NotNull private final String completeCommand;
        @Getter @NotNull private final ButtonBoundActions actions;
        @Getter private final int id;
        private final Action action;

        public final void complete() {
            if(action.disposable()) {
                actions.remove(this);
            }

            action.run();
        }
    }

    interface Action {

        void run();
        default boolean disposable() {
            return false;
        }
    }
}
