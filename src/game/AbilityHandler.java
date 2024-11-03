package game;

import game.datacollections.CardData;
import status.StatusCode;
import status.StatusOr;

import static java.lang.System.exit;

public class AbilityHandler {

    static Ability weakKnees = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedWeakKnees();
        }
    };

    static Ability skyjack = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            int casterHealth = ((CasterMinion)caster).getHealth();
            ((CasterMinion)caster).health = target.getHealth();
            target.onReceivedSkyjack(casterHealth);
        }
    };

    static Ability shapeshift = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedShapeshift();
        }
    };

    static Ability godsPlan = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedGodsPlan();
        }
    };

    static Ability subZero = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedSubZero();
        }
    };

    static Ability lowBlow = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        Minion targetMinion = targets.get(0);
        for (var target : targets) {
            if (target.getHealth() > targetMinion.getHealth()) {
                targetMinion = target;
            }
        }
        targetMinion.onReceivedLowBlow();
    };

    static Ability earthBorn = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedEarthBorn();
        }
    };

    static Ability bloodThirst = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedBloodThirst();
        }
    };

    static public StatusOr<Ability> ResolveAbility(String minionName) {
        return switch (minionName) {
            case "Disciple" -> new StatusOr<>(godsPlan);
            case "Miraj" -> new StatusOr<>(skyjack);
            case "The Cursed One" -> new StatusOr<>(shapeshift);
            case "The Ripper" -> new StatusOr<>(weakKnees);
            case "Lord Royce" -> new StatusOr<>(subZero);
            case "Empress Thorina" -> new StatusOr<>(lowBlow);
            case "King Mudface" -> new StatusOr<>(earthBorn);
            case "General Kocioraw" -> new StatusOr<>(bloodThirst);
            default -> new StatusOr<>(StatusCode.kUnknown);
        };
    }
}
