package red.oases.viptimer.Utils;

import org.bukkit.Bukkit;
import red.oases.viptimer.Extra.Enums.MessageType;
import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Objects.Privilege;

public class Privileges {
    public static void takeFromPlayer(String playername, String type, boolean withMessages) {
        Common.takePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.GIVE);
        Data.deleteRecord(playername, type);
        if (withMessages) {
            sendMessageOf(playername, type, MessageType.TAKE);
            if (Config.getBoolean("thanks")) sendMessageOf(playername, type, MessageType.THANKS);
            cancelMessageOf(playername, type, MessageType.GIVE);
        }
    }

    public static void takeFromPlayer(String playername, String type) {
        takeFromPlayer(playername, type, true);
    }

    public static void giveToPlayer(String playername, String type, boolean withMessages) {
        Common.givePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.TAKE);
        if (withMessages) {
            sendMessageOf(playername, type, MessageType.GIVE);
            if (Config.getBoolean("thanks")) sendMessageOf(playername, type, MessageType.THANKS);
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
        var message = Const.messages.get(msgType);
        var record = Data.getRecord(playername, type);

        switch (msgType) {
            case GIVE -> {
                assert record != null;
                message = message.formatted(
                        Privilege.getDisplayname(type),
                        Common.formatTimestamp(record.until())
                );
            }

            case TAKE -> message = message.formatted(
                    Privilege.getDisplayname(type)
            );
        }

        var p = Bukkit.getServer().getPlayer(playername);

        if (p == null) {
            Tasks.createMessage(
                    "%s.%s.%s".formatted(playername, type, msgType.toString()),
                    playername,
                    message
            );
        } else {
            Logs.send(p, message);
        }
    }
}
