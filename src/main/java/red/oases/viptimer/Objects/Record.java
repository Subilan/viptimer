package red.oases.viptimer.Objects;

import java.sql.Date;

public class Record extends ExpirableRecord {
    private final String created_by;
    private final Date created_at;
    private final Date updated_at;

    public Record(String playername, String type, Long until, String created_by, Date created_at, Date updated_at) {
        super(playername, type, until);
        this.created_at = created_at;
        this.created_by = created_by;
        this.updated_at = updated_at;
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
