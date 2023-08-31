package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Extra.Enums.Role;
import red.oases.viptimer.Objects.Timers.DistributionTimer;
import red.oases.viptimer.Objects.Timers.ReceiptTimer;
import red.oases.viptimer.Objects.Timers.RecordTimer;
import red.oases.viptimer.Utils.*;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("viptimer")).setTabCompleter(new Tab());
        Objects.requireNonNull(getCommand("viptimer")).setExecutor(new Executor());
        getServer().getPluginManager().registerEvents(new Events(), this);
        saveDefaultConfig();

        Const.plugin = this;
        Files.load(this.getDataFolder());
        Logs.load(this.getLogger());
        DB.load();
        RecordTimer.run();

        Const.role = Role.of(Files.config.getString("role"));
        switch (Const.role) {
            case DISTRIBUTOR -> {
                Logs.info("已准备分发数据。");
                Common.distributeTypes();
                DistributionTimer.run();
            }

            case RECEIVER -> {
                Logs.info("已准备接收数据。");
                var distribution = Data.getDistributionUnreceived();
                if (distribution != null) {
                    var instanceId = Common.getInstanceId();
                    Common.receiveTypeDistribution(distribution);
                    if (!distribution.setReceived(instanceId)) {
                        Logs.severe("Cannot mark Distribution created by " + distribution.dist_by() + " and to be received by " + instanceId + " as received.");
                    }
                }
                ReceiptTimer.run();
            }
        }

        Logs.info("VIPTimer 已加载完毕。");
    }

    @Override
    public void onDisable() {
        Logs.info("VIPTimer 已停用。");
    }
}
