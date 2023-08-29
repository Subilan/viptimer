package red.oases.viptimer.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class Data {

    public static boolean createRecord(String playername, String type, long until, CommandSender createdBy) {
        var conn = DB.getConnection();
        if (conn == null) return false;
        try (var st = conn.createStatement()) {
            st.executeQuery(
                    "INSERT INTO 'vip_records' (playername, type, until, created_by) VALUES (%s, %s, %s, %s)"
                            .formatted(playername, type, until, createdBy.getName())
            );
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            return false;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return true;
    }

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
