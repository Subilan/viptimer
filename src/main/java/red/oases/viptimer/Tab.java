package red.oases.viptimer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.oases.viptimer.Utils.Common;
import red.oases.viptimer.Utils.Files;
import red.oases.viptimer.Utils.Patterns;

import java.util.List;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class Tab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return null;
        }

        if (args.length == 1) {
            if (sender.hasPermission("viptimer.admin")) return List.of(
                    "give",
                    "take",
                    "menu",
                    "test"
            );
            else return List.of(
                    "menu"
            );
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "give", "take" -> {
                    return List.of("<playername>");
                }
            }
        }

        if (args.length == 3) {
            switch (args[0]) {
                case "give" -> {
                    return Files.config.getStringList("types");
                }
            }
        }

        if (args.length == 4) {
            switch (args[0]) {
                case "give" -> {
                    var numeric = Common.mustPositive(args[3]);
                    if (numeric != 0) {
                        return List.of(
                                numeric + "h",
                                numeric + "d",
                                numeric + "m"
                        );
                    } else if (Patterns.isDuration(args[3])) {
                        return List.of(args[3]);
                    } else {
                        return List.of();
                    }
                }
            }
        }

        return null;
    }
}
