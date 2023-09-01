package red.oases.viptimer.Objects.Timers;

import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.*;

import java.util.Date;

public class RecordTimer extends CancellableTimer {

    @Override
    protected void execute() {
        var now = new Date().getTime();
        var records = Data.getExpirableRecords();
        for (var r : records) {
            if (now > r.getUntil()) {
                var playername = r.getPlayername();
                var type = r.getType();
                Bukkit.getScheduler().runTask(Const.plugin, s -> {
                    Logic.takePlayer(playername, type);
                    Logs.info("Deleted record %s.%s due to expiration."
                            .formatted(playername, type));
                });
            }
        }
    }
}
