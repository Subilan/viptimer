package red.oases.viptimer;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import red.oases.viptimer.Objects.Menu;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.Delayed;
import red.oases.viptimer.Utils.Logs;

import static red.oases.viptimer.Utils.Common.t;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        var p = e.getPlayer();

        Delayed.doDelayedActionFor(p);
        Delayed.sendDelayedMessageFor(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onClick(InventoryClickEvent e) {
        var inv = e.getInventory();

        if (!(inv.getHolder() instanceof Menu)) return;

        e.setCancelled(true);

        if (e.getClick() != ClickType.LEFT) return;

        var clicked = e.getCurrentItem();
        if (clicked == null) return;

        var container = clicked.getItemMeta().getPersistentDataContainer();
        if (!container.has(Common.getItemStackIdentifier())) return;

        var identifier = container.get(Common.getItemStackIdentifier(), PersistentDataType.STRING);
        if (identifier == null) return;

        var p = (Player) e.getWhoClicked();

        switch (identifier) {
            case Const.II_WEBOPENER -> {
                inv.close();
                p.playSound(Const.SOUND_LEVELUP);
                Logs.send(p,
                        t("[", NamedTextColor.GRAY).append(
                                t("单击打开官方充值网页", NamedTextColor.YELLOW, TextDecoration.BOLD)
                                .clickEvent(ClickEvent.openUrl("https://www.wcfaka.com/links/6D9FA6A7"))
                                .hoverEvent(HoverEvent.showText(t("立即打开", NamedTextColor.WHITE)))
                        ).append(t("]", NamedTextColor.GRAY))
                );
            }

            case Const.II_GUIDEBOOK -> {
                inv.close();
                Logs.send(p, "手册正在编写中，敬请期待...");
            }
        }
    }
}
