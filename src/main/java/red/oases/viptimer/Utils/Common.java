package red.oases.viptimer.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Extra.Enums.TaskAction;
import red.oases.viptimer.Extra.Enums.TimeUnit;
import red.oases.viptimer.Extra.Exceptions.UnexpectedMatchException;
import red.oases.viptimer.Extra.Interfaces.StringHandler;
import red.oases.viptimer.Objects.Distribution;
import red.oases.viptimer.Objects.Privilege;
import red.oases.viptimer.Objects.Record;

import java.text.SimpleDateFormat;
import java.util.*;

public class Common {
    public static long mustPositiveLong(String target) {
        long result;
        try {
            result = Long.parseLong(target);
        } catch (NumberFormatException e) {
            return 0;
        }
        return result;
    }

    /**
     * 转换对应字符串为非负整数。如果转换失败，返回 0。
     *
     * @param target 待转换的字符串
     * @return 成功为对应整数，不成功为 0
     */
    public static int mustPositive(String target) {
        int result;
        try {
            result = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            return 0;
        }
        return result;
    }

    public static long getUntil(int number, String unit) {
        return getUntil(number, TimeUnit.from(unit));
    }

    public static long getUntil(int number, TimeUnit unit) {
        var now = new Date().getTime();
        switch (unit) {
            case HOUR -> {
                return now + number * 3_600_000L;
            }

            case DAY -> {
                return now + number * 24 * 3_600_000L;
            }

            case MONTH -> {
                return now + number * 30 * 24 * 3_600_000L;
            }
        }
        throw new UnexpectedMatchException();
    }

    public static String formatTimestamp(long epoch) {
        return formatDate(new Date(epoch));
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static boolean notType(String t) {
        return !getTypes().contains(t);
    }

    public static void executeCommands(List<String> commands, StringHandler handler) {
        for (var cmd : commands) {
            if (!Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    handler.handle(cmd)
            )) Logs.warn("Command execution failed: " + cmd);
            else Logs.info("Command execution succeeded: " + cmd);
        }
    }

    public static String getReplaced(String input, Map<String, String> replacement) {
        for (var k : replacement.keySet()) {
            input = input.replaceAll(k, replacement.get(k));
        }
        return input;
    }

    public static void takePrivileges(String player, String type) {
        executeCommands(Privilege.of(type).take(), cmd -> getReplaced(cmd, Map.of(
                "\\$playername",
                player,
                "\\$displayname",
                Privilege.getDisplayname(type)
        )));
        if (!Data.deleteDelivery(player, type)) Logs.warn("Cannot delete delivery %s.%s".formatted(player, type));
    }

    public static void givePrivileges(String player, String type) {
        executeCommands(Privilege.of(type).give(), cmd -> getReplaced(cmd, Map.of(
                "\\$playername",
                player,
                "\\$displayname",
                Privilege.getDisplayname(type)
        )));
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

    public static List<String> getTypes() {
        var section = Files.types.getConfigurationSection("types");
        if (section == null) return List.of();
        return section.getKeys(false).stream().toList();
    }

    /**
     * 获取当前插件所在服务器对应的 instance id
     * 如果不存在则会创建，储存在 types.yml 内
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getInstanceId() {
        var instId = Files.types.getString("inst_id");

        if (instId != null) {
            UUID.fromString(instId);
            // throw IllegalArgumentException if not valid.
        } else {
            instId = UUID.randomUUID().toString();
            Files.types.set("inst_id", instId);
            Files.saveTypes();
        }

        return instId;
    }

    /**
     * 将当前 <code>types.yml</code> 内容写入数据库。
     */
    public static void updateDistribution() {
        if (Data.updateDistribution(getInstanceId(), Files.types.saveToString())) {
            Logs.info("Successfully updated distribution.");
        } else {
            Logs.severe("Cannot create distribution of instance " + getInstanceId() + ".");
        }
    }

    /**
     * 将对应的 Distribution 标记为已接收，但此状态并不代表以后不会再接收。<br/>
     * 此函数将会
     * <ul>
     *     <li>在不存在 receipt 记录的时候创建，并将 recv_count 加一。</li>
     *     <li>如果已经存在，则直接将 recv_count 加一。</li>
     * </ul>
     */
    public static boolean markReceived(String distId) {
        if (Data.hasReceipt(distId)) {
            return Data.increaseRecvCount(distId);
        } else {
            if (Data.createReceipt(distId)) {
                return Data.increaseRecvCount(distId);
            } else {
                return false;
            }
        }
    }

    /**
     * 将 Distribution 对象包含的内容写入本地 <code>types.yml</code>。
     *
     * @param distribution 指定的对象
     */
    public static void writeDistribution(@NotNull Distribution distribution) {
        // Backup current instance id.
        var instanceId = getInstanceId();
        try {
            Files.types.loadFromString(distribution.dist_content());
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
        // Override incoming instance id.
        Files.types.set("inst_id", instanceId);
        Files.saveTypes();
    }

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
                        if (!Data.hasDelivery(p.getName(), targetType)) Privileges.giveToPlayer(p.getName(), targetType, false);
                        else Logs.warn("Delivery is already made. No changes were made.");
                    }

                    case TAKE -> {
                        if (Data.hasDelivery(p.getName(), targetType)) Privileges.takeFromPlayer(p.getName(), targetType, false);
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

    public static @Nullable Record getRecord(String playername) {
        for (var t : getTypes()) {
            if (Data.hasRecord(playername, t)) return Data.getRecord(playername, t);
        }

        return null;
    }

    public static NamespacedKey key(String key) {
        var result = NamespacedKey.fromString(key, Const.plugin);
        assert result != null;
        return result;
    }

    public static NamespacedKey getItemStackIdentifier() {
        return key("oasis-itemstack-identifier");
    }

    public static Component t(String text, TextColor color, TextDecoration dec) {
        return t(text, color).decorate(dec);
    }

    public static Component t(String text, TextColor color) {
        return t(text).color(color);
    }

    public static Component t(String text) {
        return Component.text(text).decorations(Map.of(
                TextDecoration.ITALIC,
                TextDecoration.State.FALSE
        ));
    }
}
