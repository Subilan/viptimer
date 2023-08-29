package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.DB;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Files.load(this.getDataFolder());
        Logs.load(this.getLogger());
        DB.load();
        Objects.requireNonNull(getCommand("viptimer")).setTabCompleter(new Tab());
        Objects.requireNonNull(getCommand("viptimer")).setExecutor(new Executor());
        getServer().getPluginManager().registerEvents(new Events(), this);
        Common.plugin = this;
    }

    @Override
    public void onDisable() {

    }
}
