package epfl.dias.sql;

import java.sql.SQLException;

/**
 * Created by torcato on 10.06.15.
 */
public class SQLFilterException extends SQLException {
    public SQLFilterException(String msg) {
        super(msg);
    }

    public SQLFilterException(Throwable cause) {
        super(cause);
    }

    public SQLFilterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}