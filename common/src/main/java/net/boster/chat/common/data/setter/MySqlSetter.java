package net.boster.chat.common.data.setter;

import net.boster.chat.common.data.ConnectedDatabase;
import org.jetbrains.annotations.NotNull;

public class MySqlSetter extends DatabaseSetter {

    public MySqlSetter(ConnectedDatabase db) {
        super(db);
    }

    @Override
    public @NotNull String getName() {
        return "MySql";
    }
}
