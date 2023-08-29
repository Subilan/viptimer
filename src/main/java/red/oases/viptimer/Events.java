package red.oases.viptimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        if (Files.uuid.getKeys(false).contains(p.getName())) return;

        Files.uuid.set(p.getName(), p.getUniqueId().toString());
        Files.saveUUID();
        Logs.info("已将玩家 " + p.getName() + " 的 UUID " + p.getUniqueId().toString() + " 写入本地文件。");
    }
}
