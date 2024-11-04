package status;

public class StatusOr<T> extends Status {

    private final T body;

    public StatusOr(final Status status) {
        this(status.statusCode, status.message);
    }

    public StatusOr(final StatusCode statusCode) {
        this(statusCode, "");
    }

    public StatusOr(final StatusCode statusCode, final String message) {
        super(statusCode, message);
        this.body = null;
    }

    public StatusOr(final T body) {
        super(StatusCode.kOk, null);
        this.body = body;
    }

    /**
     * Checks if it has body.
     *
     * @return boolean
     */
    public final boolean hasBody() {
        return body != null;
    }

    /**
     * Returns the body. Performs no null checks.
     *
     * @return T
     */
    public final T unwrap() {
        return body;
    }
}
