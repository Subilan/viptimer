package red.oases.viptimer.Utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.plugin.Plugin;
import red.oases.viptimer.Extra.Enums.Role;

public class Const {
    public static Role role;
    public static Plugin plugin;

    public static final String II_PLAYERHEAD_FREE = "oasis-playerhead-free";
    public static final String II_PLAYERHEAD_PREM = "oasis-playerhead-prem";
    public static final String II_PURCHASEOPENER = "oasis-purchase-opener";
    public static final String II_WIKIOPENER = "oasis-wiki-opener";
    public static final Sound SOUND_LEVELUP = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.AMBIENT, 1f, 1f);
    public static final Sound SOUND_ORB_PICKUP = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 1f, 1f);
    public static final Sound SOUND_BOOK_PAGE_TURN = Sound.sound(Key.key("item.book.page_turn"), Sound.Source.AMBIENT, 1f, 1f);
}
