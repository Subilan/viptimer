package red.oases.viptimer.Utils;

import org.bukkit.plugin.Plugin;
import red.oases.viptimer.Extra.Enums.MessageType;
import red.oases.viptimer.Extra.Enums.Role;

import java.util.Map;

public class Const {
    public static Role role;
    public static Plugin plugin;

    public static Map<MessageType, String> messages = Map.of(
            MessageType.GIVE,
            "你已获得 %s，有效期至 %s",
            MessageType.THANKS,
            "多谢支持！",
            MessageType.TAKE,
            "你的 %s 已过期或被删除"
    );
}
