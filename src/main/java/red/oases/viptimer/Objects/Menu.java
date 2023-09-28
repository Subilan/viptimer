package red.oases.viptimer.Objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.SkullCreator;

import java.util.List;

import static red.oases.viptimer.Utils.Common.t;
import static red.oases.viptimer.Utils.Common.tt;

public class Menu implements InventoryHolder {
    public Player p;

    public Menu(Player p) {
        this.p = p;
    }

    public static Component key(String name) {
        return t("· ", NamedTextColor.GRAY)
                .append(t(name, NamedTextColor.GREEN))
                .append(t(": ", NamedTextColor.YELLOW));
    }

    public ItemStack getDefaultHead() {
        var def = SkullCreator.itemFromUuid(p.getUniqueId());
        var meta = def.getItemMeta();
        meta.lore(List.of(
                t("Oasis 会员暂未开通", NamedTextColor.GRAY)
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_PLAYERHEAD_FREE);

        def.setItemMeta(meta);
        return def;
    }

    public ItemStack getPlayerInfo() {
        var record = Common.getRecord(p.getName());
        if (record == null) return getDefaultHead();
        var playerhead = SkullCreator.itemFromUuid(p.getUniqueId());
        var meta = playerhead.getItemMeta();
        meta.displayName(t(p.getName() + " 的会员", NamedTextColor.AQUA));
        meta.lore(List.of(
                key("会员类型").append(
                        tt(
                                record.getPrivilege().displayname()
                        )
                ),
                key("有效期至").append(
                        t(Common.formatTimestamp(record.until()), NamedTextColor.GOLD)
                )
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_PLAYERHEAD_PREM);
        playerhead.setItemMeta(meta);
        return playerhead;
    }

    public ItemStack getPurchaseOpener() {
        var opener = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0NDk4YTBmZTI3ODk1NmUzZDA0MTM1ZWY0YjEzNDNkMDU0OGE3ZTIwOGM2MWIxZmI2ZjNiNGRiYzI0MGRhOCJ9fX0=");
        var meta = opener.getItemMeta();
        meta.displayName(
                t("[", NamedTextColor.DARK_GRAY)
                        .append(t("单击查看充值页面链接", NamedTextColor.YELLOW, TextDecoration.BOLD))
                        .append(t("]", NamedTextColor.DARK_GRAY))
        );
        meta.lore(List.of(
                t(),
                t("单击获取充值页面链接", NamedTextColor.GRAY),
                t("点击后即可前往充值", NamedTextColor.GRAY)
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_WEBOPENER);

        opener.setItemMeta(meta);
        return opener;
    }

    public ItemStack getWikiOpener() {
        var book = new ItemStack(Material.BOOK);
        var meta = book.getItemMeta();
        meta.displayName(t("Oasis 会员详情", NamedTextColor.AQUA));
        meta.lore(List.of(
                t(),
                t("在此查看可用的特权！", NamedTextColor.GRAY)
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_GUIDEBOOK);
        book.setItemMeta(meta);
        return book;
    }

    public @NotNull Inventory getInventory() {
        var inv = Bukkit.createInventory(
                this,
                45,
                t("Oasis 会员中心", TextColor.color(0xEE4426))
        );

        var template = "ddddddddd" +
                "droygcbpd" +
                "drPyIcWpd" +
                "droygcbpd" +
                "ddddddddd";

        for (var i = 0; i < template.length(); i++) {
            switch (template.charAt(i)) {
                case 'd' -> inv.setItem(i, new ItemStack(Material.DIAMOND));
                case 'r' -> inv.setItem(i, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                case 'o' -> inv.setItem(i, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE));
                case 'y' -> inv.setItem(i, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
                case 'g' -> inv.setItem(i, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
                case 'c' -> inv.setItem(i, new ItemStack(Material.CYAN_STAINED_GLASS_PANE));
                case 'b' -> inv.setItem(i, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
                case 'p' -> inv.setItem(i, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
                case 'W' -> inv.setItem(i, getWikiOpener());
                case 'P' -> inv.setItem(i, getPurchaseOpener());
                case 'I' -> inv.setItem(i, getPlayerInfo());
            }
        }

        return inv;
    }

    public void open() {
        p.openInventory(getInventory());
    }
}