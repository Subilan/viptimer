package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Objects.RecordTimer;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.DB;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

import java.sql.Connection;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Files.load(this.getDataFolder());
        Logs.load(this.getLogger());
        Objects.requireNonNull(getCommand("viptimer")).setTabCompleter(new Tab());
        Objects.requireNonNull(getCommand("viptimer")).setExecutor(new Executor());
        getServer().getPluginManager().registerEvents(new Events(), this);
        RecordTimer.run(this);
        Common.plugin = this;
        Logs.info("VIPTimer 已加载完毕。");
    }

    @Override
    public void onDisable() {
        Logs.info("VIPTimer 已停用。");
    }
}
