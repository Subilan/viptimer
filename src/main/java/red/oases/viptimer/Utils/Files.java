package red.oases.viptimer.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import red.oases.viptimer.Extra.Interfaces.SectionHandler;

import java.io.File;
import java.io.IOException;

public class Files {
    public static File datafolder;
    public static File fconfig;
    public static FileConfiguration config;
    public static File ftasks;
    public static FileConfiguration tasks;

    public static void load(File datafolder) {
        Files.datafolder = datafolder;
        Files.fconfig = new File(datafolder.getAbsolutePath() + "/config.yml");
        Files.ftasks = new File(datafolder.getAbsolutePath() + "/tasks.yml");
        reload();
    }

    public static void reload() {
        Files.config = YamlConfiguration.loadConfiguration(Files.fconfig);
        Files.tasks = YamlConfiguration.loadConfiguration(Files.ftasks);
    }

    public static void saveConfig() {
        try {
            Files.config.save(Files.fconfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTasks() {
        try {
            Files.tasks.save(Files.ftasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void withSaveTasks(SectionHandler handler) {
        handler.handle(tasks);
        saveTasks();
    }

    public static void deleteAction(String uuid) {
        tasks.set("actions." + uuid, null);
        saveTasks();
    }

    public static void deleteMessage(String uuid) {
        tasks.set("messages." + uuid, null);
        saveTasks();
    }
}
