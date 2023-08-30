package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

public class CommandChtype extends Command {

    public CommandChtype(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        if (args.length < 3) {
            Logs.send(sender, "参数不足：/vipt chtype <playername> <from> <to>");
            return true;
        }

        var playername = args[1];
        var from = args[2];
        var to = args[3];

        if (!Data.hasRecord(playername, from)) {
            Logs.send(sender, playername + " 不存在 " + from + " 的记录。");
            return true;
        }

        if (!Common.isType(to)) {
            Logs.send(sender, to + " 不是有效的类型。");
            return true;
        }

        if (Data.alterRecord(playername, from, to)) {
            Logs.send(sender, "成功修改记录。");
            Logs.send(sender, "%s.%s -> %s.%s"
                    .formatted(playername, from, playername, to));
        } else {
            Logs.send(sender, "数据库操作失败。");
        }

        return true;
    }
}
