package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;
import red.oases.viptimer.Utils.Patterns;

public class CommandSet extends Command {
    public CommandSet(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        if (args.length < 4) {
            Logs.send(sender, "参数不足：/vipt set <playername> <type> <until> [new]");
            return true;
        }

        var playername = args[1];
        var type = args[2];
        var _until = args[3];
        var isNew = false;

        if (args.length >= 5) {
            isNew = args[4].equalsIgnoreCase("new");
        }

        if (!Data.hasRecord(playername, type) && !isNew) {
            Logs.send(sender, playername + " 不存在 " + type + " 的记录。");
            Logs.send(sender, "如需按此新增，请在指令结尾加上 new。");
            return true;
        }

        long until;

        if (Patterns.isNonNegativeInteger(_until)) {
            until = Common.mustPositiveLong(_until);
        } else {
            if (Patterns.isDate(_until)) {
                until = Patterns.getDate(_until).getTime();
            } else {
                Logs.send(sender, "日期或时间戳无效。");
                return true;
            }
        }

        if (Data.hasRecord(playername, type)) {
            var record = Data.getRecord(playername, type);
            assert record != null;
            if (Data.alterRecord(playername, type, until)) {
                Logs.send(sender, "成功修改记录。");
                Logs.send(sender, "%s.%s %s -> %s"
                        .formatted(
                                record.getPlayername(),
                                record.getType(),
                                Common.formatTimestamp(record.getUntil()),
                                Common.formatTimestamp(until)
                        ));
            } else {
                Logs.send(sender, "数据库操作失败。");
                return true;
            }
        } else {
            if (Data.createRecord(playername, type, until, sender)) {
                Logs.send(sender, "成功将 %s 给予 %s，有效期至 %s"
                        .formatted(type, playername, Common.formatTimestamp(until)));
            } else {
                Logs.send(sender, "数据库操作失败。");
                return true;
            }
        }

        return true;
    }
}
