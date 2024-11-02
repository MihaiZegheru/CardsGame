package game;

import game.datacollections.CardData;
import status.StatusCode;
import status.StatusOr;

public class AbilityHandler {

    static Ability weakKnees = (caster, target) -> {
        target.onReceivedWeakKnees(2);
    };

    static Ability skyjack = (caster, target) -> {
        int casterHealth = caster.getHealth();
        caster.health = target.getHealth();
        target.onReceivedSkyjack(casterHealth);
    };

    static Ability shapeshift = (caster, target) -> {
        target.onReceivedShapeshift();
    };

    static Ability godsPlan = (caster, target) -> {
        target.onReceivedGodsPlan(2);
    };

    static public StatusOr<Ability> ResolveAbility(String minionName) {
        return switch (minionName) {
            case "Disciple" -> new StatusOr<>(godsPlan);
            case "Miraj" -> new StatusOr<>(skyjack);
            case "The Cursed One" -> new StatusOr<>(shapeshift);
            case "The Ripper" -> new StatusOr<>(weakKnees);
            default -> new StatusOr<>(StatusCode.kUnknown);
        };
    }
}
