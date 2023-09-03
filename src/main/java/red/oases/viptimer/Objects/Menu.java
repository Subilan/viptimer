package red.oases.viptimer.Objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        meta.displayName(t(p.getName() + " 的会员"));
        meta.lore(List.of(
                t("· 会员类型: ").append(
                        tt(
                                switch (record.getPrivilege().type()) {
                                    case "oasisplus" -> "<gradient:#2DB5F0:#2DF0BF>OasisPlus</gradient>";
                                    case "oasislife" -> "<gradient:#A63F2C:#BC9F54>OasisLife</gradient>";
                                    default -> record.getPrivilege().displayname();
                                }
                        )
                ),
                t("· 有效期至: " + Common.formatTimestamp(record.until()))
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_PLAYERHEAD_PREM);
        playerhead.setItemMeta(meta);
        return playerhead;
    }

    public ItemStack getWebsiteOpener() {
        var opener = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0NDk4YTBmZTI3ODk1NmUzZDA0MTM1ZWY0YjEzNDNkMDU0OGE3ZTIwOGM2MWIxZmI2ZjNiNGRiYzI0MGRhOCJ9fX0=");
        var meta = opener.getItemMeta();
        meta.displayName(
                t("[", NamedTextColor.DARK_GRAY)
                        .append(t("单击查看充值页面链接", NamedTextColor.YELLOW, TextDecoration.BOLD))
                        .append(t("]", NamedTextColor.DARK_GRAY))
        );
        meta.lore(List.of(
                t("由于 MOJANG 限制", NamedTextColor.GRAY),
                t("请单击聊天框内链接", NamedTextColor.GRAY),
                t("多谢理解~", NamedTextColor.GRAY)
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_WEBOPENER);

        opener.setItemMeta(meta);
        return opener;
    }

    public ItemStack getBook() {
        var book = new ItemStack(Material.BOOK);
        var meta = book.getItemMeta();
        meta.displayName(Component.text("Oasis 会员手册"));
        meta.lore(List.of(
                t("在此查看可用的特权", NamedTextColor.GRAY),
                t("以及对一些常见问题", NamedTextColor.GRAY),
                t("的解答和帮助信息！", NamedTextColor.GRAY)
        ));
        meta.getPersistentDataContainer().set(Common.getItemStackIdentifier(), PersistentDataType.STRING, Const.II_GUIDEBOOK);
        book.setItemMeta(meta);
        return book;
    }

    public @NotNull Inventory getInventory() {
        var inv = Bukkit.createInventory(
                this,
                45,
                Component.text("Oasis VIP 信息")
        );

        var template = "ddddddddd" +
                "droygcbpd" +
                "drWyIcBpd" +
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
                case 'B' -> inv.setItem(i, getBook());
                case 'W' -> inv.setItem(i, getWebsiteOpener());
                case 'I' -> inv.setItem(i, getPlayerInfo());
            }
        }

        return inv;
    }

    public void open() {
        p.openInventory(getInventory());
    }
}