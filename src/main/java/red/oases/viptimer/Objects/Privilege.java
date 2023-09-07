package red.oases.viptimer.Objects;

import red.oases.viptimer.Utils.Files;

import java.util.List;

public record Privilege(String type, String displayname, List<String> give, List<String> take) {
    public static Privilege of(String type) {
        var section = Files.types.getConfigurationSection("types");
        if (section == null) throw new RuntimeException("Invalid name for Privilege initialization.");
        return new Privilege(
                type,
                section.getString(type + ".displayname", type),
                section.getStringList(type + ".give"),
                section.getStringList(type + ".take")
        );
    }

    public String displayname() {
        if (displayname == null) return type;
        if (displayname.isEmpty()) return type;
        return displayname;
    }

    public static String getDisplayname(String type) {
        var result = Files.types.getString("types." + type + ".displayname");
        return result == null ? type : result;
    }
}
