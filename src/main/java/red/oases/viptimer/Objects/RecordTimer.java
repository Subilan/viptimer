package red.oases.viptimer.Objects;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RecordTimer {

    public static void update() {
        var records = Data.getExpirableRecords();
        var now = new Date().getTime();
        for (var r : records) {
            if (now > r.getUntil()) {
                var playername = r.getPlayername();
                var type = r.getType();
                Logs.sendOrLater(playername, "你的 VIP（%s）已于 %s 过期".formatted(r.getType(), Common.formatTimestamp(r.getUntil())));
                Logs.sendOrLater(playername, "多谢你的支持！");
                Common.takePrivilegesOrLater(playername, type);
                Data.deleteRecord(playername, type);
                Logs.info("Deleted record %s.%s due to expiration."
                        .formatted(playername, type));
            }
        }
    }

    public static void run(Plugin plugin) {
        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, s -> {
            RecordTimer.update();
        }, 0, 1, TimeUnit.SECONDS);
        Logs.info("已注册权限到期检查计时器。");
    }
}
