package red.oases.viptimer.Extra.Interfaces;

import org.bukkit.configuration.ConfigurationSection;

@FunctionalInterface
public interface SectionHandler {
    void handle(ConfigurationSection cs);
}
