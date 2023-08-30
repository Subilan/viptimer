package red.oases.viptimer.Utils;

import java.util.List;

public class Config {

    public static String getString(String path) {
        return Files.config.getString(path);
    }

    public static Integer getInteger(String path) {
        return Files.config.getInt(path);
    }

    public static Boolean getBoolean(String path) {
        return Files.config.getBoolean(path);
    }

    public static List<String> getTypes() {
        return Files.config.getStringList("types");
    }

    public static List<String> getGiveCommands() {
        return Files.config.getStringList("commands.give");
    }

    public static List<String> getTakeCommands() {
        return Files.config.getStringList("commands.take");
    }
}
