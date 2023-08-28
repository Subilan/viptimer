package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Utils.DB;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Files.load(this.getDataFolder());
        Logs.load(this.getLogger());
        DB.load();
    }

    @Override
    public void onDisable() {

    }
}
