package red.oases.viptimer.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Extra.Interfaces.CursorHandler;
import red.oases.viptimer.Objects.ExpirableRecord;
import red.oases.viptimer.Objects.Record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Data {

    public static boolean execute(String sql) {
        var conn = DB.getConnection();
        if (conn == null) return false;
        try (var st = conn.createStatement()) {
            st.execute(sql);
            return true;
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            return false;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public static <T> T withResult(String sql, CursorHandler<T> handle) {
        var conn = DB.getConnection();
        if (conn == null) throw new RuntimeException("Cannot get connection in method `withResult`.");
        try (var st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            var result = st.executeQuery(sql);
            return handle.handler(result);
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public static List<ExpirableRecord> getExpirableRecords() {
        return withResult("SELECT (playername, type, until) FROM vip_records", r -> {
            var result = new ArrayList<ExpirableRecord>();
            try {
                while (r.next()) {
                    result.add(new ExpirableRecord(
                            r.getString("playername"),
                            r.getString("type"),
                            r.getLong("until")
                    ));
                }
                return result;
            } catch (SQLException e) {
                Logs.severe(e.getMessage());
                return List.of();
            }
        });
    }

    public static @Nullable Record getRecord(String playername, String type) {
        return withResult("SELECT * FROM vip_records WHERE playername='%s' AND type='%s'"
                .formatted(playername, type), r -> {
            try {
                if (r.next()) {
                    return new Record(
                            r.getString("playername"),
                            r.getString("type"),
                            r.getLong("until"),
                            r.getString("created_by"),
                            r.getDate("created_at"),
                            r.getDate("updated_at")
                    );
                } else {
                    return null;
                }
            } catch (SQLException e) {
                Logs.severe(e.getMessage());
                return null;
            }
        });
    }

    public static boolean createRecord(String playername, String type, long until, CommandSender createdBy) {
        return execute("INSERT INTO vip_records (playername, type, until, created_by) VALUES ('%s', '%s', %s, '%s')"
                .formatted(playername, type, until, createdBy.getName()));
    }

    public static boolean deleteRecord(String playername, String type) {
        return execute("DELETE FROM vip_records WHERE playername='%s' AND type='%s'"
                .formatted(playername, type));
    }

    public static boolean transferOwnership(String from, String fromType, String to) {
        return execute("UPDATE vip_records SET playername='%s' WHERE playername='%s' AND type='%s'"
                .formatted(to, from, fromType));
    }

    public static boolean alterRecord(String playername, String type, long until) {
        return execute("UPDATE vip_records SET until=%s WHERE playername='%s' AND type='%s'"
                .formatted(until, playername, type));
    }

    public static boolean alterRecord(String playername, String fromType, String toType) {
        return execute("UPDATE vip_records SET type='%s' WHERE playername='%s' AND type='%s'"
                .formatted(toType, playername, fromType));
    }

    public static boolean hasResult(String sql) {
        return withResult(sql, s -> {
            try {
                return s.next();
            } catch (SQLException e) {
                Logs.severe("Cannot determine if the result is present.");
                Logs.severe(e.getMessage());
                return false;
            }
        });
    }

    /**
     * 判断指定玩家是否有任何 VIP 记录
     */
    public static boolean hasRecord(String playername) {
        return hasResult("SELECT * FROM vip_records WHERE playername='%s'".formatted(playername));
    }

    /**
     * 判断指定玩家是否有指定类型的 VIP 记录
     */
    public static boolean hasRecord(String playername, String type) {
        return hasResult("SELECT * FROM vip_records WHERE playername='%s' AND type='%s'".formatted(playername, type));
    }
}
