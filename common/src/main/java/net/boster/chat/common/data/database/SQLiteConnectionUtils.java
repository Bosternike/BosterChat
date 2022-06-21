package net.boster.chat.common.data.database;

import net.boster.chat.common.BosterChat;
import net.boster.chat.common.log.LogType;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.*;

public class SQLiteConnectionUtils extends SQLDatabase {

    public boolean connect() {
        try {
            if(connection != null && !connection.isClosed()) {
                return false;
            }
            BosterChat.get().log("Connecting to database...", LogType.INFO);
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + new File(BosterChat.get().getDataFolder(), "users.db").toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            createTableIfNotExists();
            BosterChat.get().log("Database connection done!", LogType.FINE);
            return true;
        } catch (SQLException | IllegalArgumentException e) {
            BosterChat.get().log("Could not connect database!", LogType.ERROR);
            return false;
        }
    }

    public void setMySqlUserValue(String key, String arg1, String arg2) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO `users` (uuid, " + arg1 + ") VALUES ('" + key + "', '" + arg2 + "') " +
                        "ON CONFLICT (uuid) DO UPDATE SET " + arg1 + " = excluded." + arg1);
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
