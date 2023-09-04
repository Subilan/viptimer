package red.oases.viptimer.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Patterns {

    public static final String INPUT_DATE_PATTERN = "yyyy-MM-dd_HH:mm:ss";

    public static final Pattern DURATION = Pattern.compile("^(-?\\d+)([hdm])$");
    public static final Pattern NON_NEGATIVE_INTEGER = Pattern.compile("^\\d+$");

    public static boolean isNonNegativeInteger(String input) {
        return NON_NEGATIVE_INTEGER.matcher(input).matches();
    }

    public static Date getDate(String input) {
        try {
            return new SimpleDateFormat(INPUT_DATE_PATTERN).parse(input);
        } catch (ParseException e) {
            throw new AssertionError();
        }
    }

    public static boolean isDate(String input) {
        try {
            new SimpleDateFormat(INPUT_DATE_PATTERN).parse(input);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

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
