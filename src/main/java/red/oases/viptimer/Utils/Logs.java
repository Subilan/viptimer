package red.oases.viptimer.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logs {
    public static Logger logger;

    public static void severe(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public static void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }
    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void load(Logger logger) {
        Logs.logger = logger;
    }

    public static Component defaultPrefix() {
        return Component.text("[").color(NamedTextColor.GREEN)
                .append(Component.text("VIPTimer").color(NamedTextColor.YELLOW))
                .append(Component.text("]").color(NamedTextColor.GREEN))
                .appendSpace();
    }

    public static void send(CommandSender sender, Component comp) {
        sender.sendMessage(defaultPrefix().append(comp));
    }

    public static void send(CommandSender sender, String text) {
        sender.sendMessage(defaultPrefix().append(Component.text(text).color(NamedTextColor.WHITE)));
    }
}
