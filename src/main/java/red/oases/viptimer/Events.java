package red.oases.viptimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import red.oases.viptimer.Utils.Common;
public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        Common.doDelayedActionFor(p);
        Common.sendDelayedMessageFor(p);
    }
}
