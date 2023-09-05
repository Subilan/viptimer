package red.oases.viptimer.Utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
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
            Config.getString("thank-you-text", ""),
            MessageType.TAKE,
            "你的 %s 已过期或被删除"
    );

    public static final String II_PLAYERHEAD_FREE = "oasis-playerhead-free";
    public static final String II_PLAYERHEAD_PREM = "oasis-playerhead-prem";
    public static final String II_WEBOPENER = "oasis-webopener";
    public static final String II_GUIDEBOOK = "oasis-guidebook";
    public static final Sound SOUND_LEVELUP = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.AMBIENT, 1f, 1f);
    public static final Sound SOUND_ORB_PICKUP = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 1f, 1f);
}
