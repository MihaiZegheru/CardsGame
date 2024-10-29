package status;

public class StatusOr<T> extends Status {

    private final T body;

    public StatusOr(Status status) {
        this(status.statusCode, "");
    }

    public StatusOr(StatusCode statusCode) {
        this(statusCode, "");
    }

    public StatusOr(StatusCode statusCode, String message) {
        super(statusCode, message);
        this.body = null;
    }

    public StatusOr(T body) {
        super(StatusCode.kOk, null);
        this.body = body;
    }

    public boolean hasBody() {
        return body != null;
    }

    public T getBody() { return body; }
}
