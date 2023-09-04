package red.oases.viptimer.Extra.Enums;

import java.util.List;

public enum TimeUnit {
    HOUR,
    DAY,
    MONTH;

    public static boolean isValid(String s) {
        return List.of("h", "H", "d", "D", "m", "M").contains(s);
    }

    public static TimeUnit from(String s) {
        return switch (s) {
            case "h", "H" -> HOUR;
            case "d", "D" -> DAY;
            case "m", "M" -> MONTH;
            default -> throw new IllegalArgumentException("Illegal TimeUnit");
        };
    }
}
