package red.oases.viptimer.Objects;

public class ExpirableRecord {

    private final String playername;
    private final String type;
    private final Long until;

    public ExpirableRecord(String playername, String type, Long until) {
        this.playername = playername;
        this.type = type;
        this.until = until;
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
}
