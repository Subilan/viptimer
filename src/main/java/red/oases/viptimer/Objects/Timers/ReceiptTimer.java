package red.oases.viptimer.Objects.Timers;

import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;

/**
 * <b>Receipt Timer</b><br/><br/>
 * <p>
 * 提供一个可重复运行的任务，此任务用于检查当前实例中已经 receive 的相关 distribution 是否已经被修改，或者是否有新的 distribution 需要接收，
 * 如果是，则将数据库内的最新数据同步到本地，并在 receipt 表中更新相关记录。
 */
public class ReceiptTimer extends CancellableTimer {
    protected void execute() {
        for (var d : Data.getDistributions()) {
            Common.writeDistribution(d);
            if (!Common.markReceived(d.dist_by())) {
                Logs.severe("Cannot make incrementation to recv_count. dist_by=%s, recv_by=%s".formatted(d.dist_by(), Common.getInstanceId()));
            }
            Logs.info("Distribution synchronized successfully - Updated at %s and received at %s."
                    .formatted(Common.formatDate(d.updated_at()), Common.formatDate(new Date())));
        }
    }
}
