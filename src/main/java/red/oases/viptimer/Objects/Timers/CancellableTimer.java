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

    public void runSync(long interval, TimeUnit unit) {
        syncTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Const.plugin, new Runnable() {
            @Override
            public void run() {
                if (!DB.isClosed()) {
                    execute();
                }
            }
        }, 0, interval * 20);
    }
}
