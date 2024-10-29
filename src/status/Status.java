package status;

import java.util.Objects;

public class Status {

    protected final StatusCode statusCode;
    protected final String message;

    public boolean isOk() {
        return statusCode == StatusCode.kOk;
    }

    public static Status ok() {
        return new Status(StatusCode.kOk, null);
    }

    public Status(StatusCode statusCode) {
        this(statusCode, "");
    }

    public Status(StatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = Objects.requireNonNullElse(message, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(statusCode, status.statusCode);
    }

    @Override
    public String toString() {
        return message;
    }
}
