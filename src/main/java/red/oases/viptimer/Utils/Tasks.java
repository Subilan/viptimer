package red.oases.viptimer.Utils;

import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Extra.Interfaces.SectionHandler;

import java.util.UUID;

public class Tasks {
    public static void create(String sectionName, SectionHandler handler) {
        var uuid = UUID.randomUUID().toString();
        Files.withSaveTasks(t -> {
            var section = t.createSection(sectionName + "." + uuid);
            handler.handle(section);
        });
    }

    public static void createAction(String playername, String type, TaskAction actionType) {
        create("actions", section -> {
            section.set("target_player", playername);
            section.set("target_type", type);
            section.set("action", actionType.toString());
        });
    }

    public static void createMessage(String playername, String message) {
        create("messages", section -> {
            section.set("target_player", playername);
            section.set("message", message);
        });
    }
}
