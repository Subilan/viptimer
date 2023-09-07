package red.oases.viptimer.Utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import red.oases.viptimer.Extra.Enums.TaskAction;

import java.util.Objects;

public class Delayed {
    /**
     * 读取本地 <code>tasks.yml</code> 内的待发消息，并发送给玩家 p。
     */
    public static void sendDelayedMessageFor(Player p) {
        var messages = Files.tasks.getConfigurationSection("messages");

        if (messages == null) return;

        for (var k : messages.getKeys(false)) {
            var msg = messages.getConfigurationSection(k);
            if (msg == null) continue;

            try {
                var targetPlayer = Objects.requireNonNull(msg.getString("target_player"));
                if (!targetPlayer.equalsIgnoreCase(p.getName())) continue;
                var message = Objects.requireNonNull(msg.getString("message"));

                Logs.msend(p, message);

                Logs.info("Delivery success! Message sent for player " + targetPlayer + ".");
                Files.deleteMessage(k);
                Logs.info("Deleted message record " + k + ".");
            } catch (NullPointerException e) {
                Logs.severe("Could not find enough data when iterating through MESSAGES.");
                Logs.severe("Problem key: " + k + ".");
            }
        }
    }

    /**
     * 读取本地 <code>tasks.yml</code> 内的任务，并为玩家 p 执行。
     */
    public static void doDelayedActionFor(@NotNull Player p) {
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
                        if (!Data.hasDelivery(p.getName(), targetType))
                            Privileges.giveToPlayer(p.getName(), targetType, false);
                        else Logs.warn("Delivery is already made. No changes were made.");
                    }

                    case TAKE -> {
                        if (Data.hasDelivery(p.getName(), targetType))
                            Privileges.takeFromPlayer(p.getName(), targetType, false);
                        else Logs.warn("Delivery is already removed. No changes were made.");
                    }
                }

                Logs.info("Delayed action finished with [action=%s, targetType=%s, targetPlayer=%s]."
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
}
