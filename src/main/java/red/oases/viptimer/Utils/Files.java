package red.oases.viptimer.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Files {
    public static File datafolder;
    public static File fconfig;
    public static File fuuid;
    public static FileConfiguration config;
    public static FileConfiguration uuid;

    public static void load(File datafolder) {
        Files.datafolder = datafolder;
        Files.fconfig = new File(datafolder.getAbsolutePath() + "/config.yml");
        Files.config = YamlConfiguration.loadConfiguration(Files.fconfig);
        Files.fuuid = new File(datafolder.getAbsolutePath() + "/uuid.yml");
        Files.uuid = YamlConfiguration.loadConfiguration(Files.fuuid);
    }

    public static void saveConfig() {
        try {
            Files.config.save(Files.fconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUUID() {
        try {
            Files.uuid.save(Files.fuuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
