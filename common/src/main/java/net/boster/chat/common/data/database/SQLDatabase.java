package net.boster.chat.common.data.database;

import lombok.Getter;
import net.boster.chat.common.data.ConnectedDatabase;

import java.sql.*;

public abstract class SQLDatabase implements ConnectedDatabase {

    @Getter protected Connection connection;

    public abstract boolean connect();

    public void createTableIfNotExists() {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (`uuid` VARCHAR(36), `data` TEXT(1000000000), PRIMARY KEY (`uuid`))");
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setMySqlUserValue(String key, String arg1, String arg2) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("INSERT INTO `users` (uuid, " + arg1 + ") VALUES ('" + key + "', '" + arg2 + "') ON DUPLICATE KEY UPDATE " + arg1 + " = '" + arg2 + "'");
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteUser(String key) {
        new DatabaseRunnable().run(() -> {
            try {
                PreparedStatement st = connection.prepareStatement("DELETE FROM `users` WHERE uuid = '" + key + "'");
                st.execute();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public String getMySqlValue(String key, String arg) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE uuid = '" + key + "'");
            ResultSet rs = statement.executeQuery();
            String r = null;
            if(rs.next()) {
                r = rs.getString(arg);
            }
            rs.close();
            statement.close();
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
