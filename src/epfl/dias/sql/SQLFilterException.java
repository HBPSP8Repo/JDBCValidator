package epfl.dias.sql;


/**
 * Created by torcato on 10.06.15.
 * Exception when a query does no pass the check
 */
public class SQLFilterException extends ValidatorException {
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