package red.oases.viptimer;

import org.bukkit.plugin.java.JavaPlugin;
import red.oases.viptimer.Extra.Enums.Role;
import red.oases.viptimer.Objects.Timers.DeliveryTimer;
import red.oases.viptimer.Objects.Timers.DistributionTimer;
import red.oases.viptimer.Objects.Timers.ReceiptTimer;
import red.oases.viptimer.Objects.Timers.RecordTimer;
import red.oases.viptimer.Utils.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public final class Main extends JavaPlugin {
    public static RecordTimer recordTimer;
    public static DistributionTimer distributionTimer;
    public static ReceiptTimer receiptTimer;
    public static DeliveryTimer deliveryTimer;

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
        recordTimer = new RecordTimer();
        distributionTimer = new DistributionTimer();
        receiptTimer = new ReceiptTimer();
        deliveryTimer = new DeliveryTimer();

        deliveryTimer.runAsync(1, TimeUnit.SECONDS);
        Logs.info("已注册权限同步检查重复逻辑。");
        recordTimer.runSync(1, TimeUnit.SECONDS);
        Logs.info("已注册权限到期检查重复逻辑。");

        Const.role = Role.of(Files.config.getString("role"));
        switch (Const.role) {
            case DISTRIBUTOR -> {
                Common.updateDistribution();
                distributionTimer.runAsync(1, TimeUnit.SECONDS);
                Logs.info("已注册数据分发重复逻辑。");
            }

            case RECEIVER -> {
                receiptTimer.runAsync(1, TimeUnit.SECONDS);
                Logs.info("已注册数据接收重复逻辑。");
            }
        }

        Logs.info("VIPTimer 已加载完毕。");
    }

    @Override
    public void onDisable() {
        Logs.info("VIPTimer 已停用。");
        deliveryTimer.cancelAsync();
        receiptTimer.cancelAsync();
        distributionTimer.cancelAsync();
        recordTimer.cancelSync();
        DB.close();
        Logs.info("数据库资源已释放。");
    }
}
