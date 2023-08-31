package red.oases.viptimer.Objects.Timers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import red.oases.viptimer.Utils.Const;
import red.oases.viptimer.Utils.DB;

import java.util.concurrent.TimeUnit;

public abstract class CancellableTimer {
    public static ScheduledTask task;

    protected abstract void execute();

    public void cancel() {
        task.cancel();
    }

    public void run(long interval, TimeUnit unit) {
        task = Bukkit.getAsyncScheduler().runAtFixedRate(Const.plugin, t -> {
            if (!DB.isClosed()) {
                execute();
            }
        }, 0, interval, unit);
    }
}
