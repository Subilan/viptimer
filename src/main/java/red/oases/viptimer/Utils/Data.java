package red.oases.viptimer.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {

    public static @Nullable ResultSet getResultSet(String sql) {
        var conn = DB.getConnection();
        if (conn == null) return null;
        try (var st = conn.createStatement()) {
            return st.executeQuery(sql);
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            return null;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public static boolean createRecord(String playername, String type, long until, CommandSender createdBy) {
        var res = getResultSet("INSERT INTO 'vip_records' (playername, type, until, created_by) VALUES ('%s', '%s', %s, '%s')"
                .formatted(playername, type, until, createdBy.getName()));
        return res != null;
    }

    /**
     * 判断指定玩家是否有任何 VIP 记录
     */
    public static boolean hasRecord(String playername) {
        var result = getResultSet("SELECT * FROM 'vip_records' WHERE playername='%s'"
                .formatted(playername));
        if (result == null) return false;
        try {
            return result.first();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 判断指定玩家是否有指定类型的 VIP 记录
     */
    public static boolean hasRecord(String playername, String type) {
        var conn = DB.getConnection();
        if (conn == null) return false;
        try (var st = conn.createStatement()) {
            var result = st.executeQuery(
                    "SELECT * FROM 'vip_records' WHERE playername='%s' AND type='%s'"
                            .formatted(playername, type)
            );
            return result.first();
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            return false;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
