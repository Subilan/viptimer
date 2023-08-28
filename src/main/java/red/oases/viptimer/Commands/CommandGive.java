package red.oases.viptimer.Commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Config;
import red.oases.viptimer.Utils.Logs;
import red.oases.viptimer.Utils.Patterns;

public class CommandGive extends Command {
    public CommandGive(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        if (args.length < 4) {
            Logs.send(sender, "参数不足：/vipt give <player> <type> <duration>");
            return true;
        }

        var player = args[1];
        var type = args[2];
        var duration = args[3];

        if (!Config.getTypes().contains(type)) {
            Logs.send(sender, "错误：类型 " + type + " 不存在。");
            return true;
        }

        if (!Patterns.isDuration(duration)) {
            Logs.send(sender, "错误：持续时间格式不正确。");
            return true;
        }

        var durationNumber = Common.mustPositive(Patterns.getDuration(duration)[0]);
        var durationUnit = Patterns.getDuration(duration)[1];

        if (durationNumber == 0) {
            Logs.send(sender, "错误：持续时间必须为正数");
            return true;
        }

        return true;
    }
}
