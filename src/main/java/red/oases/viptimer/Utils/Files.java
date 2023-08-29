package red.oases.viptimer.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Files {
    public static File datafolder;
    public static File fconfig;
    public static FileConfiguration config;

    public static void load(File datafolder) {
        Files.datafolder = datafolder;
        Files.fconfig = new File(datafolder.getAbsolutePath() + "/config.yml");
        reload();
    }

    public static void reload() {
        Files.config = YamlConfiguration.loadConfiguration(Files.fconfig);
    }

    public static void saveConfig() {
        try {
            Files.config.save(Files.fconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
