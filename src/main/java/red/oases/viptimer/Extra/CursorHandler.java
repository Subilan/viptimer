package red.oases.viptimer.Extra;

import java.sql.ResultSet;

@FunctionalInterface
public interface CursorHandler<T> {
    T run(ResultSet consume);
}
