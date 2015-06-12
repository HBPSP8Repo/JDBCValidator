package epfl.dias.sql;

/**
 * Created by torcato on 12-06-2015.
 * Exception for malformed urls
 */
public class InvalidUrlException extends ValidatorException {
    public InvalidUrlException(String msg) {
        super(msg);
    }

    public InvalidUrlException(Throwable cause) {
        super(cause);
    }

    public InvalidUrlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}