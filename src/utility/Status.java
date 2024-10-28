package utility;

import java.util.Objects;

public class Status {

    enum StatusCode {
        kOk("Ok."),
        kAborted("Aborted."),
        kUnknown("Unknown."),
        kOutOfRange("Index out of range."),
        kNotEnoughManaToPlace("Not enough mana to place card on table."),
        ;

        private final String message;
        StatusCode(String message) {
            this.message = message;
        }
        public String getMessage() { return message; }
    }

    private final StatusCode statusCode;

    public boolean isOk() {
        return statusCode == StatusCode.kOk;
    }

    public static Status ok() {
        return new Status(StatusCode.kOk);
    }

    public static Status aborted() {
        return new Status(StatusCode.kAborted);
    }

    public static Status outOfRange() {
        return new Status(StatusCode.kOutOfRange);
    }

    public static Status notEnoughManaToPlace() {
        return new Status(StatusCode.kNotEnoughManaToPlace);
    }

    private Status(StatusCode statusCode) {
        this.statusCode = statusCode;
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
        return statusCode.getMessage();
    }
}
