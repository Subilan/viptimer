package red.oases.viptimer.Commands;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.oases.viptimer.Command;
import red.oases.viptimer.Extra.Annotations.DisableConsole;
import red.oases.viptimer.Extra.Annotations.PermissionLevel;
import red.oases.viptimer.Objects.Menu;

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
        p.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 1f, 1f));
        return true;
    }
}
