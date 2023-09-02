package red.oases.viptimer.Objects;

import java.util.Date;

public record Record (String playername, String type, Long until, String created_by, Date created_at, Date updated_at) {
    public Privilege getPrivilege() {
        return Privilege.of(type);
    }
}
