package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Objects.Privilege;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logic;
import red.oases.viptimer.Utils.Logs;

public class CommandTransown extends Command {
    public CommandTransown(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        if (args.length < 4) {
            Logs.send(sender, "参数不足：/vipt transown <from> <from-type> <to>");
            return true;
        }

        var from = args[1];
        var fromType = args[2];
        var to = args[3];

        if (!Data.hasRecord(from, fromType)) {
            Logs.send(sender, from + " 不存在 " + fromType + " 的记录。");
            return true;
        }

        if (Data.transferOwnership(from, fromType, to)) {
            Logs.send(sender, "操作成功。");
            Logs.send(sender, "%s.%s -> %s.%s"
                    .formatted(from, fromType, to, fromType));

            Logic.takePlayer(from, fromType);
            Logic.givePlayer(to, fromType);
        } else {
            Logs.send(sender, "数据库操作失败。");
        }

        return true;
    }
}
