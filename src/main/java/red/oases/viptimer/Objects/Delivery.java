package red.oases.viptimer.Objects;

import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Extra.Interfaces.SectionHandler;
import red.oases.viptimer.Utils.Files;

import java.util.UUID;

public class Delivery {

    private static void createTask(String sectionName, SectionHandler handler) {
        var uuid = UUID.randomUUID().toString();
        Files.withSaveTasks(t -> {
            var section = t.createSection(sectionName + "." + uuid);

            handler.handle(section);
        });
    }

    public static void doLater(String playername, String type, TaskAction actionType) {
        createTask("tasks", section -> {
            section.set("target_player", playername);
            section.set("target_type", type);
            section.set("action", actionType.toString());
        });
    }

    public static void sendLater(String playername, String message) {
        createTask("messages", section -> {
            section.set("target_player", playername);
            section.set("message", message);
        });
    }
}
