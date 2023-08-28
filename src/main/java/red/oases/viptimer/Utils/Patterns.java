package red.oases.viptimer.Utils;

import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern DURATION = Pattern.compile("^(\\d+)([hdm])$");

    /**
     * 判断是否是合法的 Duration
     */
    public static boolean isDuration(String input) {
        return DURATION.matcher(input).find();
    }

    public static String[] getDuration(String input) {
        return new String[]{
                DURATION.matcher(input).group(1),
                DURATION.matcher(input).group(2)
        };
    }
}
