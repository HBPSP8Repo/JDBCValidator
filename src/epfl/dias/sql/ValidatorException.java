package epfl.dias.sql;

import java.sql.SQLException;

/**
 * Created by torcato on 12-06-2015.
 * Base class for all validator exceptions
 */
public class ValidatorException extends SQLException {
    public ValidatorException(String msg) {
        super(msg);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}