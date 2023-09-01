package red.oases.viptimer.Objects.Timers;

import org.bukkit.Bukkit;
import red.oases.viptimer.Objects.Privilege;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logic;
import red.oases.viptimer.Utils.Logs;

/**
 * <b>Delivery Timer</b><br/><br/>
 * 提供一个可重复运行的任务，此任务检测在线玩家的跨服权限发放（give/take 的执行）情况
 */
public class DeliveryTimer extends CancellableTimer {
    @Override
    protected void execute() {
        for (var p : Bukkit.getServer().getOnlinePlayers()) {
            for (var t : Common.getTypes()) {
                if (Data.hasRecord(p.getName(), t) && !Data.hasDelivery(p.getName(), t)) {
                    Logs.info(p.getName() + "'s " + t + " is not delivered on this server.");
                    Logs.info("Delivery completed!");
                    Logic.givePlayer(p.getName(), t);
                }

                if (!Data.hasRecord(p.getName(), t) && Data.hasDelivery(p.getName(), t)) {
                    Logs.info(p.getName() + "'s " + t + " is not removed on this server.");
                    Logs.info("Removal completed!");
                    Logic.takePlayer(p.getName(), t);
                }
            }
        }
    }
}
