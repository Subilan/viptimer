package red.oases.viptimer.Objects;

import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <b>Receipt Timer</b><br/><br/>
 * <p>
 * 提供一个可重复运行的任务，此任务用于检查当前实例中已经 receive 的相关 distribution 是否已经被修改，如果是，则将数据库内的最新数据同步到本地。
 */
public class ReceiptTimer {

    public static void run() {
        Bukkit.getAsyncScheduler().runAtFixedRate(Const.plugin, t -> {
            var updated = Data.getDistributionUpdated();
            if (updated == null) return;
            Common.receiveTypeDistribution(updated);
            if (!Data.increment("receipt", "recv_count", "dist_by='%s' AND recv_by='%s'"
                    .formatted(updated.dist_by(), Common.getInstanceId()))) {
                Logs.severe("Cannot make incrementation to recv_count. dist_by=%s, recv_by=%s".formatted(updated.dist_by(), Common.getInstanceId()));
            }
            Logs.info("Distribution synchronized successfully - Updated at %s and received at %s."
                    .formatted(Common.formatDate(updated.updated_at()), Common.formatDate(new Date())));
        }, 0, 1, TimeUnit.SECONDS);
    }
}
