package red.oases.viptimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        var name = p.getName();
        var uuid = p.getUniqueId().toString();
    }
}
