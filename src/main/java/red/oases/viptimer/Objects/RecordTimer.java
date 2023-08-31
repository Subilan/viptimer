package red.oases.viptimer.Objects;

import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecordTimer {

    public static void update(List<ExpirableRecord> records) {
        var now = new Date().getTime();
        for (var r : records) {
            if (now > r.getUntil()) {
                var playername = r.getPlayername();
                var type = r.getType();
                Bukkit.getScheduler().runTask(Const.plugin, s -> {
                    Logs.sendOrLater(playername, "你的 VIP（%s）已于 %s 过期".formatted(r.getType(), Common.formatTimestamp(r.getUntil())));
                    Logs.sendOrLater(playername, "多谢你的支持！");
                    Common.takePrivilegesOrLater(playername, type);
                    Data.deleteRecord(playername, type);
                    Logs.info("Deleted record %s.%s due to expiration."
                            .formatted(playername, type));
                });

            }
        }
    }

    public static void run() {
        Bukkit.getAsyncScheduler().runAtFixedRate(Const.plugin, s -> {
            RecordTimer.update(Data.getExpirableRecords());
        }, 0, 1, TimeUnit.SECONDS);
        Logs.info("已注册权限到期检查计时器。");
    }
}
