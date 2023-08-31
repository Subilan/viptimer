package red.oases.viptimer.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Extra.Interfaces.CursorHandler;
import red.oases.viptimer.Objects.Distribution;
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

    public static boolean increment(String table, String column, String whereCondition) {
        return withResult("SELECT %s FROM %s WHERE %s".formatted(table, column, whereCondition), r -> {
            try {
                while (r.next()) {
                    var currentVal = r.getInt(column);
                    if (!execute("UPDATE %s SET %s=%s".formatted(table, column, currentVal + 1))) return false;
                }
                return true;
            } catch (SQLException e) {
                Logs.severe(e.getMessage());
                return false;
            }
        });
    }

    public static List<ExpirableRecord> getExpirableRecords() {
        return withResult("SELECT playername, type, until FROM vip_records", r -> {
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

    public static boolean updateDistribution(String instId, String distContent) {
        if (hasResult("SELECT * FROM distribution WHERE dist_by='%s'".formatted(instId))) {
            return execute("UPDATE distribution SET dist_content='%s'".formatted(distContent));
        } else {
            return execute("INSERT INTO distribution (dist_by, dist_content) VALUES ('%s', '%s')"
                    .formatted(instId, distContent));
        }
    }

    public static @Nullable Distribution getDistributionUpdated() {
        // Select all the distributions that were updated after they were lastly received by some other instance.
        // This should only generate one or zero result.
        return withResult("SELECT * FROM distribution, receipt WHERE distribution.updated_at > receipt.recv_by AND distribution.dist_by = receipt.dist_by", r -> {
            try {
                if (r.next()) {
                    return new Distribution(
                            r.getString("dist_by"),
                            r.getString("dist_content"),
                            r.getDate("updated_at"),
                            r.getDate("created_at")
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

    public static @Nullable Distribution getDistributionUnreceived() {
        // Select all the distributions that were not received by current instance.
        // This should only generate one or zero result.
        return withResult("SELECT * FROM distribution WHERE NOT dist_by IN (SELECT dist_by FROM receipt WHERE recv_by='%s')"
                .formatted(Common.getInstanceId()), r -> {
            try {
                if (r.next()) {
                    return new Distribution(
                            r.getString("dist_by"),
                            r.getString("dist_content"),
                            r.getDate("updated_at"),
                            r.getDate("created_at")
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

    public static boolean setDelivered(String playername, String type, boolean d) {
        return execute("UPDATE vip_records SET delivered=%s WHERE playername='%s' AND type='%s'"
                .formatted(d, playername, type));
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
