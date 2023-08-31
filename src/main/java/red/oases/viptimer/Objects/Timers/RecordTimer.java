package red.oases.viptimer.Objects.Timers;

import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;

public class RecordTimer extends CancellableTimer {

    protected void execute() {
        var now = new Date().getTime();
        var records = Data.getExpirableRecords();
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
}
