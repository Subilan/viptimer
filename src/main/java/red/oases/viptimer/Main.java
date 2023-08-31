package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Extra.Enums.Role;
import red.oases.viptimer.Objects.RecordTimer;
import red.oases.viptimer.Utils.*;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Const.plugin = this;
        Files.load(this.getDataFolder());
        Logs.load(this.getLogger());
        DB.load();
        Objects.requireNonNull(getCommand("viptimer")).setTabCompleter(new Tab());
        Objects.requireNonNull(getCommand("viptimer")).setExecutor(new Executor());
        getServer().getPluginManager().registerEvents(new Events(), this);
        RecordTimer.run();
        Const.role = Role.of(Files.config.getString("role"));
        if (Const.role.isDistributor()) {
            Logs.info("已准备分发数据。");
        } else if (Const.role.isReceiver()) {
            Logs.info("已准备接收数据。");
        }
        Common.transferTypes();
        Logs.info("VIPTimer 已加载完毕。");
    }

    @Override
    public void onDisable() {
        Logs.info("VIPTimer 已停用。");
    }
}
