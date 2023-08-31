package red.oases.viptimer.Extra.Enums;

import org.jetbrains.annotations.Nullable;

public enum Role {
    DISTRIBUTOR,
    RECEIVER,
    NONE;

    public boolean isDistributor() {
        return this == DISTRIBUTOR;
    }

    public boolean isReceiver() {
        return this == RECEIVER;
    }

    public boolean isNone() {
        return this == NONE;
    }

    public static Role of(@Nullable String s) {
        if (s == null) return NONE;
        if (s.equalsIgnoreCase("distributor")) return DISTRIBUTOR;
        if (s.equalsIgnoreCase("receiver")) return RECEIVER;
        return NONE;
    }
}
