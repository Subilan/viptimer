package red.oases.viptimer.Utils;

import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern DURATION = Pattern.compile("^(\\d+)([hdm])$");

    /**
     * 判断是否是合法的 Duration
     */
    public static boolean isDuration(String input) {
        return DURATION.matcher(input).matches();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String[] getDuration(String input) {
        var matcher = DURATION.matcher(input);
        matcher.matches();
        return new String[]{
                matcher.group(1),
                matcher.group(2)
        };
    }
}
