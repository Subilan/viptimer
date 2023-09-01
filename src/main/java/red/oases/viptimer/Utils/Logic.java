package red.oases.viptimer.Utils;

import red.oases.viptimer.Extra.Enums.TaskAction;

public class Logic {
    public static void takePlayer(String playername, String type) {
        Common.takePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.GIVE);
        Data.deleteRecord(playername, type);
        Data.deleteDelivery(playername, type);
    }

    public static void givePlayer(String playername, String type) {
        Common.givePrivilegesOrLater(playername, type);
        Tasks.cancelAction(playername, type, TaskAction.TAKE);
        Data.createDelivery(playername, type);
    }
}
