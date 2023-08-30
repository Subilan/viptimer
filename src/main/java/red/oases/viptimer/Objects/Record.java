package red.oases.viptimer.Objects;

import java.io.Serializable;
import java.sql.Date;

public class Record implements Serializable {
    private final String playername;
    private final String type;
    private final Long until;
    private final String created_by;
    private final Date created_at;
    private final Date updated_at;

    public Record(String playername, String type, Long until, String created_by, Date created_at, Date updated_at) {
        this.playername = playername;
        this.type = type;
        this.until = until;
        this.created_at = created_at;
        this.created_by = created_by;
        this.updated_at = updated_at;
    }

    public String getPlayername() {
        return this.playername;
    }

    public String getType() {
        return this.type;
    }

    public Long getUntil() {
        return this.until;
    }

    public String getCreatedBy() {
        return this.created_by;
    }

    public Date getCreatedAt() {
        return this.created_at;
    }

    public Date getUpdatedAt() {
        return this.updated_at;
    }
}
