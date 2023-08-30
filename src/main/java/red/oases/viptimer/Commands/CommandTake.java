package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

public class CommandTake extends Command {
    public CommandTake(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        if (args.length < 3) {
            Logs.send(sender, "参数不足：/vipt take <playername> <type>");
            return true;
        }

        var playername = args[1];
        var type = args[2];

        if (!Common.isType(type)) {
            Logs.send(sender, type + " 不是有效的类型。");
            return true;
        }

        if (!Data.hasRecord(playername, type)) {
            Logs.send(sender, playername + " 不存在 " + type + " 的相关记录。");
            return true;
        }

        if (Data.deleteRecord(playername, type)) {
            Logs.send(sender, "已删除 " + playername + " 的 " + type + " 记录。");
        } else {
            Logs.send(sender, "删除失败，请检查控制台。");
        }

        return true;
    }
}
