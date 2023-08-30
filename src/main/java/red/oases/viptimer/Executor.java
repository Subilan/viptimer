package red.oases.viptimer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import red.oases.viptimer.Commands.CommandGive;
import red.oases.viptimer.Commands.CommandReload;
import red.oases.viptimer.Commands.CommandSet;

public class Executor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!label.equalsIgnoreCase("viptimer") && !label.equalsIgnoreCase("vipt")) {
            return true;
        }

        if (args.length == 0) {
            return true;
        }

        switch (args[0]) {
            case "give" -> {
                return new CommandGive(args, sender).collect();
            }

            case "set" -> {
                return new CommandSet(args, sender).collect();
            }

            case "reload" -> {
                return new CommandReload(args, sender).collect();
            }
        }

        return true;
    }
}
