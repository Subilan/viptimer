package red.oases.viptimer.Objects;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Extra.Interfaces.SectionHandler;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

import java.util.Objects;
import java.util.UUID;

public class Delivery {

    public static void sendMessagesFor(Player p) {
        var messages = Files.tasks.getConfigurationSection("messages");

        if (messages == null) return;

        for (var k : messages.getKeys(false)) {
            var msg = messages.getConfigurationSection(k);
            if (msg == null) continue;

            try {
                var targetPlayer = Objects.requireNonNull(msg.getString("target_player"));
                if (!targetPlayer.equalsIgnoreCase(p.getName())) continue;
                var message = Objects.requireNonNull(msg.getString("message"));

                Logs.send(p, message);

                Logs.info("Delivery success! Message sent for player " + targetPlayer + ".");
                Files.deleteMessage(k);
                Logs.info("Deleted message record " + k + ".");
            } catch (NullPointerException e) {
                Logs.severe("Could not find enough data when iterating through MESSAGES.");
                Logs.severe("Problem key: " + k + ".");
            }
        }
    }

    public static void doActionsFor(@NotNull Player p) {
        var actions = Files.tasks.getConfigurationSection("actions");

        if (actions == null) return;

        for (var k : actions.getKeys(false)) {
            var act = actions.getConfigurationSection(k);
            if (act == null) continue;
            try {
                var targetPlayer = Objects.requireNonNull(act.getString("target_player"));
                if (!targetPlayer.equalsIgnoreCase(p.getName())) continue;
                var targetType = Objects.requireNonNull(act.getString("target_type"));
                var action = TaskAction.valueOf(act.getString("action"));

                switch (action) {
                    case GIVE -> {
                        Common.givePrivileges(p.getName(), targetType);
                    }

                    case TAKE -> {
                        Common.takePrivileges(p.getName(), targetType);
                    }
                }

                Logs.info("Delivery success! action=%s, targetType=%s, targetPlayer=%s."
                        .formatted(action, targetType, targetPlayer));
                Files.deleteAction(k);
                Logs.info("Deleted action record " + k + ".");
            } catch (NullPointerException e) {
                Logs.severe("Could not find enough data when iterating through ACTIONS.");
                Logs.severe("Problem key: " + k + ".");
            } catch (IllegalArgumentException e) {
                Logs.severe("Invalid string form of TaskAction, please do not alter anything without confirmation!");
                Logs.severe("Problem key: " + k + ".");
            }
        }
    }

    private static void createTask(String sectionName, SectionHandler handler) {
        var uuid = UUID.randomUUID().toString();
        Files.withSaveTasks(t -> {
            var section = t.createSection(sectionName + "." + uuid);

            handler.handle(section);
        });
    }

    public static void doLater(String playername, String type, TaskAction actionType) {
        createTask("actions", section -> {
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
