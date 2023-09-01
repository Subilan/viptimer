package red.oases.viptimer.Utils;

import org.bukkit.Bukkit;
import red.oases.viptimer.Extra.Enums.MessageType;
import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Objects.Privilege;

public class Logic {


    public static void takePlayer(String playername, String type) {
        Common.takePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.GIVE);
        Data.deleteRecord(playername, type);
        Data.deleteDelivery(playername, type);
        sendMessagePatternOrLater(playername, type, MessageType.TAKE);
        if (Config.getBoolean("thanks")) sendMessagePatternOrLater(playername, type, MessageType.THANKS);
        cancelMessagePattern(playername, type, MessageType.GIVE);
    }

    public static void givePlayer(String playername, String type) {
        Common.givePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.TAKE);
        Data.createDelivery(playername, type);
        sendMessagePatternOrLater(playername, type, MessageType.GIVE);
        if (Config.getBoolean("thanks")) sendMessagePatternOrLater(playername, type, MessageType.THANKS);
        cancelMessagePattern(playername, type, MessageType.TAKE);
    }

    public static void cancelMessagePattern(String playername, String type, MessageType msgType) {
        Tasks.cancelMessage("%s.%s.%s".formatted(playername, type, msgType.toString()));
    }

    public static void sendMessagePatternOrLater(String playername, String type, MessageType msgType) {
        var message = Const.messages.get(msgType);
        var record = Data.getRecord(playername, type);

        switch (msgType) {
            case GIVE -> {
                assert record != null;
                message = message.formatted(
                        Privilege.getDisplayname(type),
                        Common.formatTimestamp(record.getUntil())
                );
            }

            case TAKE -> {
                message = message.formatted(
                        Privilege.getDisplayname(type)
                );
            }
        }

        var p = Bukkit.getServer().getPlayer(playername);

        if (p == null) {
            Tasks.createMessage(
                    "%s.%s.%s".formatted(playername, type, msgType.toString()),
                    playername,
                    Const.messages.get(msgType)
            );
        } else {
            Logs.send(p, message);
        }
    }
}
