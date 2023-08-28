package red.oases.viptimer.Utils;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static String URL;

    public static void load() {
        URL = "jdbc:mysql://%s:%s/%s".formatted(
                Config.getString("database.host"),
                Config.getString("database.port"),
                Config.getString("database.db")
        );
    }

    public static @Nullable Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    URL,
                    Config.getString("database.username"),
                    Config.getString("database.password")
            );
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
