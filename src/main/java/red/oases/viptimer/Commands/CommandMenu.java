package red.oases.viptimer.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.oases.viptimer.Command;
import red.oases.viptimer.Extra.Annotations.DisableConsole;
import red.oases.viptimer.Extra.Annotations.PermissionLevel;
import red.oases.viptimer.Objects.Menu;
import red.oases.viptimer.Utils.Const;

@DisableConsole
@PermissionLevel(0)
public class CommandMenu extends Command {
    public CommandMenu(String[] args, CommandSender sender) {
        super(args, sender);
    }

    @Override
    protected boolean execute() {
        var p = (Player) sender;
        new Menu(p).open();
        p.playSound(Const.SOUND_ORB_PICKUP);
        return true;
    }
}
