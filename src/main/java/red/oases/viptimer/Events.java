package red.oases.viptimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import red.oases.viptimer.Objects.Privilege;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        Common.doDelayedActionFor(p);
        Common.sendDelayedMessageFor(p);

        if (Data.hasRecord(p.getName())) {
            for (var t : Common.getTypes()) {
                if (Data.hasRecord(p.getName(), t) && !Data.hasDelivery(p.getName(), t)) {
                    var record = Data.getRecord(p.getName(), t);
                    assert record != null;
                    Logs.info(p.getName() + "'s " + t +" is not delivered on this server.");
                    Common.givePrivileges(p.getName(), t);
                    Logs.send(p, "你已获得 %s，有效期至 %s".formatted(
                            Privilege.of(t).displayname(),
                            Common.formatTimestamp(record.getUntil())
                    ));
                    Logs.send(p, "多谢支持！");
                    Logs.info("Delivery completed!");
                    Data.createDelivery(p.getName(), t);
                }

                if (!Data.hasRecord(p.getName(), t) && Data.hasDelivery(p.getName(), t)) {
                    Logs.info(p.getName() + "'s " + t + " is not removed on this server.");
                    Common.takePrivileges(p.getName(), t);
                    Logs.send(p, "你的 VIP（%s）已过期。"
                            .formatted(Privilege.of(t).displayname()));
                    Logs.info("Removal completed!");
                    Data.deleteDelivery(p.getName(), t);
                }
            }
        }
    }
}
