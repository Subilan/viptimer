package red.oases.viptimer.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public class DB {
    public static HikariDataSource dataSource;

    public static void load() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://%s:%s/%s".formatted(
                Config.getString("database.host"),
                Config.getString("database.port"),
                Config.getString("database.db")
        ));
        config.setUsername(Config.getString("database.username"));
        config.setPassword(Config.getString("database.password"));
        dataSource = new HikariDataSource(config);
    }

    public static @Nullable Connection getConnection() {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
