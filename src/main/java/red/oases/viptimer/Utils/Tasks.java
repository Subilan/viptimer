package red.oases.viptimer.Utils;

import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Extra.Interfaces.SectionHandler;

import java.util.Objects;
import java.util.UUID;

public class Tasks {

    public static void cancelMessage(String identifier) {
        var messages = Files.tasks.getConfigurationSection("messages");
        if (messages == null) return;

        for (var k : messages.getKeys(false)) {
            try {
                if (Objects.requireNonNull(messages.getString(k + ".identifier")).equalsIgnoreCase(identifier)) {
                    Files.withSaveTasks(t -> t.set("messages." + k, null));
                }
            } catch (NullPointerException ignored) {}
        }
    }

    public static void cancelAction(String playername, String type, TaskAction actionType) {
        var actions = Files.tasks.getConfigurationSection("actions");
        if (actions == null) return;

        for (var k : actions.getKeys(false)) {
            try {
                if (Objects.requireNonNull(actions.getString(k + ".target_player")).equalsIgnoreCase(playername)
                        && Objects.requireNonNull(actions.getString(k + ".target_type")).equalsIgnoreCase(type)
                        && Objects.requireNonNull(actions.getString(k + ".action")).equalsIgnoreCase(actionType.toString())) {
                    Files.withSaveTasks(t -> t.set("actions." + k, null));
                }
            } catch (NullPointerException ignored) {}
        }
    }

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

    public static void createMessage(String identifier, String playername, String message) {
        create("messages", section -> {
            section.set("target_player", playername);
            section.set("message", message);
            section.set("identifier", identifier);
        });
    }
}
