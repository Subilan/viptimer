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
                var p = Bukkit.getServer().getPlayer(r.getPlayername());
                if (p != null) {
                    Logs.send(p, "你的 VIP（%s）已于 %s 过期".formatted(r.getType(), Common.formatTimestamp(r.getUntil())));
                    Logs.send(p, "多谢你的支持！");
                    Common.takePrivileges(p);
                }
                Data.deleteRecord(r.getPlayername(), r.getType());
                Logs.info("Deleted record %s.%s due to expiration."
                        .formatted(r.getPlayername(), r.getType()));
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
