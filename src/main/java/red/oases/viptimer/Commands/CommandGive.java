package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Extra.Enums.MessageType;
import red.oases.viptimer.Extra.Enums.TimeUnit;
import red.oases.viptimer.Utils.*;

import java.util.Date;

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

        var durationNumber = Common.mustNumeric(Patterns.getDuration(duration)[0]);
        var durationUnit = Patterns.getDuration(duration)[1];

        if (!TimeUnit.isValid(durationUnit)) {
            Logs.send(sender, "错误：时间单位不正确");
            return true;
        }

        if (Data.hasRecord(player, type)) {
            var record = Data.getRecord(player, type);
            assert record != null;
            var until = Common.getUntil(record.until(), durationNumber, durationUnit);

            if (until <= new Date().getTime()) {
                Logs.send(sender, "错误：结束时间不得小于当前时间");
                return true;
            }

            if (Data.setUntil(player, type, until)) {
                Logs.send(sender, "成功将 %s 的 %s %s到 %s"
                        .formatted(player, type, durationNumber > 0 ? "延长" : "缩短", Common.formatTimestamp(until)));

                Privileges.refreshMessagePattern(player, type, MessageType.GIVE);
            } else {
                Logs.send(sender, "错误：无法修改记录，请查看控制台");
            }

            return true;
        }

        if (durationNumber <= 0) {
            Logs.send(sender, "错误：持续时间必须为正数");
            return true;
        }

        var until = Common.getUntil(durationNumber, durationUnit);

        if (Data.createRecord(player, type, until, sender)) {
            Logs.send(sender, "成功将 %s 给予 %s，有效期至 %s"
                    .formatted(type, player, Common.formatTimestamp(until)));

            Privileges.giveToPlayer(player, type);
        } else {
            Logs.send(sender, "插入数据时发生问题，请检查后台信息。");
        }

        return true;
    }
}
