package red.oases.viptimer.Objects;

import red.oases.viptimer.Utils.Files;

import java.util.List;

public class Privilege {
    private final String type;
    private final String displayname;
    private final List<String> give;
    private final List<String> take;

    public Privilege(String type, String displayname, List<String> give, List<String> take) {
        this.type = type;
        this.displayname = displayname;
        this.give = give;
        this.take = take;
    }

    public static Privilege of(String type) {
        var section = Files.config.getConfigurationSection("types." + type);
        if (section == null) throw new RuntimeException("Invalid name for Privilege initialization.");
        return new Privilege(
                type,
                section.getString("displayname"),
                section.getStringList("give"),
                section.getStringList("take")
        );
    }

    public String getType() {
        return type;
    }

    public String getDisplayname() {
        return displayname;
    }

    public List<String> getGive() {
        return give;
    }

    public List<String> getTake() {
        return take;
    }
}
