package utility;

public class StatusOr<T> extends Status {

    private final T body;

    protected StatusOr(StatusCode statusCode) {
        super(statusCode);
        this.body = null;
    }

    protected StatusOr(T body) {
        super(StatusCode.kNone);
        this.body = body;
    }

    public boolean hasBody() {
        return body != null;
    }

    public T getBody() { return body; }
}
