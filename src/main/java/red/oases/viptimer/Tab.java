package red.oases.viptimer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

            }
        }

        return null;
    }
}
