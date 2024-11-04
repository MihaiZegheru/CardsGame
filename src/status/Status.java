package status;

import java.util.Objects;

public class Status {

    protected final StatusCode statusCode;
    protected final String message;

    public final boolean isOk() {
        return statusCode == StatusCode.kOk;
    }

    public Status(final StatusCode statusCode) {
        this(statusCode, "");
    }

    public Status(final StatusCode statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = Objects.requireNonNullElse(message, "");
    }

    /**
     * Returns OK Status.
     *
     * @return Status
     */
    public static Status ok() {
        return new Status(StatusCode.kOk, null);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Status status = (Status) o;
        return Objects.equals(statusCode, status.statusCode);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final String toString() {
        return message;
    }
}
