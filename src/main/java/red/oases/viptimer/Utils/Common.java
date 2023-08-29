package red.oases.viptimer.Utils;

import red.oases.viptimer.Extra.Enums.TimeUnit;
import red.oases.viptimer.Extra.Exceptions.UnexpectedMatchException;

import java.util.Date;

public class Common {
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

    public static boolean isPlayer(String playername) {
        return Files.uuid.getKeys(false).contains(playername);
    }

    public static String getUUID(String playername) {
        return Files.uuid.getString(playername);
    }
}
