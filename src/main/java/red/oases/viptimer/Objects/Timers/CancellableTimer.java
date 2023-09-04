package red.oases.viptimer.Objects.Timers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.DB;

import java.util.concurrent.TimeUnit;

public abstract class CancellableTimer {
    public static ScheduledTask asyncTask;
    public static int syncTask;

    protected abstract void execute();

    public void cancelAsync() {
        asyncTask.cancel();
    }

    public void cancelSync() {
        Bukkit.getScheduler().cancelTask(syncTask);
    }

    public void runAsync(long interval, TimeUnit unit) {
        asyncTask = Bukkit.getAsyncScheduler().runAtFixedRate(Const.plugin, t -> {
            if (!DB.isClosed()) {
                execute();
            }
        }, 0, interval, unit);
    }

    /**
     * 以同步模式运行重复任务
     * @param interval 重复时间间隔，单位：秒
     */
    public void runSync(long interval) {
        syncTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Const.plugin, () -> {
            if (!DB.isClosed()) {
                execute();
            }
        }, 0, interval * 20);
    }
}
