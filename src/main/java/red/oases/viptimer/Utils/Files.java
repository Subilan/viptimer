package red.oases.viptimer.Utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Files {
    public static File datafolder;
    public static File fconfig;
    public static FileConfiguration config;
    public static File ftasks;
    public static FileConfiguration tasks;
    public static File ftypes;
    public static FileConfiguration types;

    public static void load(File datafolder) {
        Files.datafolder = datafolder;
        Files.fconfig = new File(datafolder.getAbsolutePath() + "/config.yml");
        Files.ftasks = new File(datafolder.getAbsolutePath() + "/tasks.yml");
        Files.ftypes = new File(datafolder.getAbsolutePath() + "/types.yml");
        reload();
    }

    public static void reload() {
        Files.config = YamlConfiguration.loadConfiguration(Files.fconfig);
        Files.tasks = YamlConfiguration.loadConfiguration(Files.ftasks);
        Files.types = YamlConfiguration.loadConfiguration(Files.ftypes);
    }

    public static void saveTasks() {
        try {
            Files.tasks.save(Files.ftasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTypes() {
        try {
            Files.types.save(Files.ftypes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void withSaveTasks(Consumer<ConfigurationSection> handler) {
        handler.accept(tasks);
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
