package red.oases.viptimer.Extra.Enums;

import red.oases.viptimer.Extra.Exceptions.UnexpectedMatchException;

import java.util.List;

public enum TimeUnit {
    HOUR,
    DAY,
    MONTH;

    public static boolean isValid(String s) {
        return List.of("h", "H", "d", "D", "m", "M").contains(s);
    }

    public static TimeUnit from(String s) {
        if (s.equalsIgnoreCase("h")) return HOUR;
        if (s.equalsIgnoreCase("d")) return DAY;
        if (s.equalsIgnoreCase("m")) return MONTH;
        throw new UnexpectedMatchException();
    }
}
