package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Extra.Enums.TimeUnit;
import red.oases.viptimer.Utils.*;

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

        if (!Common.getTypes().contains(type)) {
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

        if (!TimeUnit.isValid(durationUnit)) {
            Logs.send(sender, "错误：时间单位不正确");
            return true;
        }

        if (Data.hasRecord(player, type)) {
            Logs.send(sender, "此玩家已有 " + type + "，请尝试更改或删除。");
            return true;
        }

        var until = Common.getUntil(durationNumber, durationUnit);

        if (Data.createRecord(player, type, until, sender)) {
            Logs.send(sender, "成功将 %s 给予 %s，有效期至 %s"
                    .formatted(type, player, Common.formatTimestamp(until)));

            Logic.givePlayer(player, type);
        } else {
            Logs.send(sender, "插入数据时发生问题，请检查后台信息。");
        }

        return true;
    }
}
