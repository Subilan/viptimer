package red.oases.viptimer.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Extra.Enums.TimeUnit;
import red.oases.viptimer.Objects.Privilege;
import red.oases.viptimer.Objects.Record;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

public class Common {

    public static List<String> getOnlinePlayerNames() {
        return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
    }

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
            result = Integer.parseUnsignedInt(target);
            return result;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 转换对应字符串为整数。如果转换失败，返回 0。
     */
    public static int mustNumeric(String target) {
        int result;
        try {
            result = Integer.parseInt(target);
            return result;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long getUntil(long number, String unit) {
        return getUntil(number, TimeUnit.from(unit));
    }

    public static long getUntil(long current, long delta, String unit) {
        return current + getDelta(delta, TimeUnit.from(unit));
    }

    public static long getDelta(long delta, TimeUnit unit) {
        return switch (unit) {
            case HOUR -> delta * 3_600_000L;
            case DAY -> delta * 24 * 3_600_000L;
            case MONTH -> delta * 30 * 24 * 3_600_000L;
        };
    }

    public static long getUntil(long number, TimeUnit unit) {
        return new Date().getTime() + getDelta(number, unit);
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

    public static void executeCommands(List<String> commands, Function<String, String> handler) {
        for (var cmd : commands) {
            if (!Bukkit.getServer().dispatchCommand(
                    Bukkit.getConsoleSender(),
                    handler.apply(cmd)
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

    public static String replaced(String input, String displayname, String playername) {
        return input
                .replaceAll("\\$displayname", displayname)
                .replaceAll("\\$raw_displayname", MiniMessage.miniMessage().stripTags(displayname))
                .replaceAll("\\$playername", playername);
    }

    public static List<String> getTypes() {
        var section = Files.types.getConfigurationSection("types");
        if (section == null) return List.of();
        return section.getKeys(false).stream().toList();
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

    public static Component t() {
        return Component.empty();
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
        )).color(NamedTextColor.WHITE);
    }

    public static Component tt(String mini) {
        return MiniMessage.miniMessage().deserialize(mini);
    }
}
