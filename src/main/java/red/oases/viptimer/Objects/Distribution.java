package red.oases.viptimer.Objects;

import red.oases.viptimer.Utils.Data;
import red.oases.viptimer.Utils.Logs;

import java.util.Date;

public record Distribution(String dist_by, String dist_content, Date updated_at, Date created_at) {
    public boolean setReceived(String recv_id) {
        if (!Data.increment("receipt", "recv_count", "dist_by='%s' AND recv_id='%s'".formatted(dist_by, recv_id))) {
            Logs.severe("Cannot make incrementation to recv_count. dist_by=%s, recv_by=%s".formatted(dist_by, recv_id));
        }
        return Data.execute("INSERT INTO receipt (dist_by, recv_by) VALUES ('%s', '%s')"
                .formatted(dist_by, recv_id));
    }
}
