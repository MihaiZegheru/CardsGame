package utility;

import java.util.Objects;

public class Status {

    enum StatusCode {
        kOk("Ok."),
        kAborted("Aborted."),
        kUnknown("Unknown."),
        kOutOfRange("Index out of range."),
        kNotEnoughManaToPlace("Not enough mana to place card on table."),
        kOwnsAttackedCard("Attacked card does not belong to the enemy."),
        kNotOwnAttackerCard("Attacker card does not belong to player at turn."),
        kCardIsFrozen("Attacker card is frozen."),
        kCardHasAttacked("Attacker card has already attacked this turn."),
        kCardNotFoundAtLocation("Card not found at location"),
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

    public static Status ownsAttackedCard() {
        return new Status(StatusCode.kOwnsAttackedCard);
    }

    public static Status notOwnAttackerCard() {
        return new Status(StatusCode.kNotOwnAttackerCard);
    }

    public static Status cardIsFrozen() {
        return new Status(StatusCode.kCardIsFrozen);
    }

    public static Status cardHasAttacked() {
        return new Status(StatusCode.kCardHasAttacked);
    }

    public static Status cardNotFoundAtLocation() {
        return new Status(StatusCode.kCardNotFoundAtLocation);
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
