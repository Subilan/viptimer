package red.oases.viptimer.Objects.Timers;

import org.bukkit.configuration.file.YamlConfiguration;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Files;


/**
 * <b>Distribution Timer</b><br/><br/>
 * 提供一个可重复运行的任务，此任务用来检查 types 文件是否被更改。如果是，那么将更改后的文件更新到数据库内。
 */
public class DistributionTimer extends CancellableTimer {

    protected void execute() {
        if (Files.types.saveToString().equals(YamlConfiguration.loadConfiguration(Files.ftypes).saveToString())) return;
        Files.types = YamlConfiguration.loadConfiguration(Files.ftypes);
        Common.updateDistribution();
    }
}
