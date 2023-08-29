package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import red.oases.viptimer.Command;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Logs;

public class CommandReload extends Command {
    public CommandReload(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        Files.reload();

        Logs.send(sender, "已刷新内存中的文件。");
        return true;
    }
}
