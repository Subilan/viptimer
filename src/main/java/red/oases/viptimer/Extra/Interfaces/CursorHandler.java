package red.oases.viptimer.Extra.Interfaces;

import java.sql.ResultSet;

@FunctionalInterface
public interface CursorHandler<T> {
    T handler(ResultSet consume);
}
