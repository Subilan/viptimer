package red.oases.viptimer.Utils;

public class Config {

    public static String getString(String path) {
        return Files.config.getString(path);
    }

    public static String getString(String path, String def) {
        return Files.config.getString(path, def);
    }

    public static Integer getInteger(String path) {
        return Files.config.getInt(path);
    }

    public static Boolean getBoolean(String path) {
        return Files.config.getBoolean(path);
    }

}
