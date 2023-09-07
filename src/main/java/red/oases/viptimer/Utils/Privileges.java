package red.oases.viptimer.Utils;

import org.bukkit.Bukkit;
import red.oases.viptimer.Extra.Enums.MessageType;
import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Objects.Privilege;

import java.util.Map;

public class Privileges {
    public static void takeFromPlayer(String playername, String type, boolean withMessages) {
        takePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.GIVE);
        Data.deleteRecord(playername, type);
        if (withMessages) {
            sendMessageOf(playername, type, MessageType.TAKE);
            sendMessageOf(playername, type, MessageType.THANKS);
            cancelMessageOf(playername, type, MessageType.GIVE);
        }
    }

    public static void takeFromPlayer(String playername, String type) {
        takeFromPlayer(playername, type, true);
    }

    public static void giveToPlayer(String playername, String type, boolean withMessages) {
        givePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.TAKE);
        if (withMessages) {
            sendMessageOf(playername, type, MessageType.GIVE);
            sendMessageOf(playername, type, MessageType.THANKS);
            cancelMessageOf(playername, type, MessageType.TAKE);
        }
    }

    public static void giveToPlayer(String playername, String type) {
        giveToPlayer(playername, type, true);
    }

    public static boolean hasMessageOf(String playername, String type, MessageType msgType) {
        return Tasks.hasMessage("%s.%s.%s".formatted(playername, type, msgType.toString()));
    }

    public static void refreshMessageOf(String playername, String type, MessageType msgType) {
        if (!hasMessageOf(playername, type, msgType)) return;
        cancelMessageOf(playername, type, msgType);
        sendMessageOf(playername, type, msgType);
    }

    public static void cancelMessageOf(String playername, String type, MessageType msgType) {
        Tasks.cancelMessage("%s.%s.%s".formatted(playername, type, msgType.toString()));
    }

    public static void sendMessageOf(String playername, String type, MessageType msgType) {
        var record = Data.getRecord(playername, type);
        String message;

        switch (msgType) {
            case GIVE -> {
                assert record != null;
                message = "你已获得 %s，有效期至 %s".formatted(
                        Privilege.getDisplayname(type),
                        Common.formatTimestamp(record.until())
                );
            }

            case TAKE -> message = "你的 %s 已过期或被删除".formatted(
                    Privilege.getDisplayname(type)
            );

            case THANKS -> message = Common.replaced(Config.getString("thank-you-text", ""), playername, type);

            default -> throw new IllegalArgumentException();
        }

        if (message.isEmpty()) return;

        var p = Bukkit.getServer().getPlayer(playername);

        if (p == null) {
            Tasks.createMessage(
                    "%s.%s.%s".formatted(playername, type, msgType.toString()),
                    playername,
                    message
            );
        } else {
            Logs.msend(p, message);
        }
    }

    public static void takePrivileges(String player, String type) {
        Common.executeCommands(Privilege.of(type).take(), cmd -> Common.replaced(cmd, Privilege.getDisplayname(type), player));
        if (!Data.deleteDelivery(player, type)) Logs.warn("Cannot delete delivery %s.%s".formatted(player, type));
    }

    public static void givePrivileges(String player, String type) {
        Common.executeCommands(Privilege.of(type).give(), cmd -> Common.replaced(cmd, Privilege.getDisplayname(type), player));
        if (!Data.createDelivery(player, type)) Logs.warn("Cannot create delivery %s.%s".formatted(player, type));
    }

    public static void takePrivilegesOrLater(String player, String type) {
        var p = Bukkit.getPlayer(player);
        if (p != null) {
            takePrivileges(player, type);
        } else {
            Logs.info("A 'TAKE' Delivery is scheduled for %s.%s due to target-offline.".formatted(player, type));
            Tasks.createAction(player, type, TaskAction.TAKE);
        }
    }

    public static void givePrivilegesOrLater(String player, String type) {
        var p = Bukkit.getPlayer(player);
        if (p != null) {
            givePrivileges(player, type);
        } else {
            Logs.info("A 'GIVE' Delivery is scheduled for %s.%s due to target-offline.".formatted(player, type));
            Tasks.createAction(player, type, TaskAction.GIVE);
        }
    }
}
