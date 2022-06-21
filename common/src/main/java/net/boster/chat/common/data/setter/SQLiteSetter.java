package net.boster.chat.common.data.setter;

import net.boster.chat.common.data.ConnectedDatabase;
import org.jetbrains.annotations.NotNull;

public class SQLiteSetter extends DatabaseSetter {

    public SQLiteSetter(ConnectedDatabase db) {
        super(db);
    }

    @Override
    public @NotNull String getName() {
        return "SQLite";
    }
}
