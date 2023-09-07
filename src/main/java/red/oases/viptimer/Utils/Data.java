package red.oases.viptimer.Utils;

import org.apache.commons.dbutils.DbUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Objects.Distribution;
import red.oases.viptimer.Objects.ExpirableRecord;
import red.oases.viptimer.Objects.Record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public static <T> T withResult(String sql, Function<ResultSet, T> handle) {
        var conn = DB.getConnection();
        if (conn == null) throw new RuntimeException("Cannot get connection in method `withResult`.");
        try (var st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            var result = st.executeQuery(sql);
            return handle.apply(result);
        } catch (SQLException e) {
            Logs.severe(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public static boolean increaseRecvCount(String distId) {
        return increment("receipt", "recv_count", "dist_by='%s' AND recv_by='%s'".formatted(distId, Synchronization.getInstanceId()));
    }

    public static boolean increment(String table, String column, String whereCondition) {
        return withResult("SELECT %s FROM %s WHERE %s".formatted(column, table, whereCondition), r -> {
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
        return withResult("SELECT playername, type, until FROM records", r -> {
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

    public static @Nullable Record getRecord(String playername) {
        return withResult("SELECT * FROM records WHERE playername='%s'"
                .formatted(playername), r -> {
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

    public static @Nullable Record getRecord(String playername, String type) {
        return withResult("SELECT * FROM records WHERE playername='%s' AND type='%s'"
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

    /**
     * 从数据库中<b>目前所需</b>的 Distribution 对象<br/><br/>
     * <p>
     * 目前所需的是指已经经过更新的或者尚未接收到的对象。
     */
    public static List<Distribution> getDistributions() {

        Function<ResultSet, List<Distribution>> collectDistribution = r -> {
            var result = new ArrayList<Distribution>();
            try {
                while (r.next()) {
                    result.add(new Distribution(
                            r.getString("dist_by"),
                            r.getString("dist_content"),
                            r.getDate("updated_at"),
                            r.getDate("created_at")
                    ));
                }
            } catch (SQLException e) {
                Logs.severe(e.getMessage());
            }
            return result;
        };

        // Select all the distributions that were updated after they were lastly received by some other instance.
        // This should only generate one or zero result.
        var distNotUpdated = withResult(
                "SELECT * FROM distribution, receipt WHERE distribution.updated_at > receipt.recv_at AND distribution.dist_by = receipt.dist_by",
                collectDistribution
        );
        // Select all the distributions that were not received by the current instance.
        // This should only generate one or zero result.
        var distNotReceived = withResult(
                "SELECT * FROM distribution WHERE NOT dist_by IN (SELECT dist_by FROM receipt WHERE recv_by='%s')".formatted(Synchronization.getInstanceId()),
                collectDistribution
        );

        return Stream.concat(distNotReceived.stream(), distNotUpdated.stream()).toList();
    }

    public static boolean createDelivery(String playername, String type) {
        return execute("INSERT INTO delivery (playername, type, inst_id) VALUES ('%s', '%s', '%s')"
                .formatted(playername, type, Synchronization.getInstanceId()));
    }

    public static boolean deleteDelivery(String playername, String type) {
        return execute("DELETE FROM delivery WHERE playername='%s' AND type='%s' AND inst_id='%s'"
                .formatted(playername, type, Synchronization.getInstanceId()));
    }

    public static boolean hasDelivery(String playername, String type) {
        return hasResult("SELECT * FROM delivery WHERE playername='%s' AND type='%s' AND inst_id='%s'"
                .formatted(playername, type, Synchronization.getInstanceId()));
    }

    public static boolean hasReceipt(String distId) {
        return hasResult("SELECT * FROM receipt WHERE dist_by='%s' AND recv_by='%s'".formatted(distId, Synchronization.getInstanceId()));
    }

    public static boolean createReceipt(String distId) {
        return execute("INSERT INTO receipt (dist_by, recv_by) VALUES ('%s', '%s')".formatted(distId, Synchronization.getInstanceId()));
    }


    public static boolean createRecord(String playername, String type, long until, CommandSender createdBy) {
        return execute("INSERT INTO records (playername, type, until, created_by) VALUES ('%s', '%s', %s, '%s')"
                .formatted(playername, type, until, createdBy.getName()));
    }

    public static boolean deleteRecord(String playername, String type) {
        return execute("DELETE FROM records WHERE playername='%s' AND type='%s'"
                .formatted(playername, type));
    }

    public static boolean transferOwnership(String from, String fromType, String to) {
        return execute("UPDATE records SET playername='%s' WHERE playername='%s' AND type='%s'"
                .formatted(to, from, fromType));
    }

    public static boolean alterRecord(String playername, String type, long until) {
        return execute("UPDATE records SET until=%s WHERE playername='%s' AND type='%s'"
                .formatted(until, playername, type));
    }

    public static boolean alterRecord(String playername, String fromType, String toType) {
        return execute("UPDATE records SET type='%s' WHERE playername='%s' AND type='%s'"
                .formatted(toType, playername, fromType));
    }

    /**
     * 判断给出的 SQL 语句是否有执行结果
     */
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
     * 判断指定玩家是否有指定类型的 VIP 记录
     */
    public static boolean hasRecord(String playername, String type) {
        return hasResult("SELECT * FROM records WHERE playername='%s' AND type='%s'".formatted(playername, type));
    }

    public static boolean hasRecord(String playername) {
        return hasResult("SELECT * FROM records WHERE playername='%s'".formatted(playername));
    }

    public static boolean setUntil(String playername, String type, long until) {
        return execute("UPDATE records SET until=%s WHERE playername='%s' AND type='%s'"
                .formatted(until, playername, type));
    }
}
