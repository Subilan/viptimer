package red.oases.viptimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        var name = p.getName();
        var uuid = p.getUniqueId().toString();

        if (Data.hasRecord(p.getName())) {
            if (Data.updateRecordIdentifier(name, uuid)) {
                Logs.info("已为玩家 %s 更新数据库名称-UUID 映射 %s -> %s".formatted(name, name, uuid));
            } else {
                Logs.severe("由于数据库操作失败，无法为玩家 %s 更新数据库名称-UUID 映射，这将导致权限判断问题。");
            }
        }
    }
}
