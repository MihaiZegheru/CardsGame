package game;

import status.StatusCode;
import status.StatusOr;

import static java.lang.System.exit;

public final class AbilityHandler {

    private static final Ability WEAK_KNEES = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedWeakKnees();
        }
    };

    private static final Ability SKYJACK = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            int casterHealth = ((CasterMinion) caster).getHealth();
            ((CasterMinion) caster).health = target.getHealth();
            target.onReceivedSkyjack(casterHealth);
        }
    };

    private static final Ability SHAPE_SHIFT = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedShapeShift();
        }
    };

    private static final Ability GODS_PLAN = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedGodsPlan();
        }
    };

    private static final Ability SUB_ZERO = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedSubZero();
        }
    };

    private static final Ability LOW_BLOW = (caster, targets) -> {
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

    private static final Ability EARTH_BORN = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedEarthBorn();
        }
    };

    private static final Ability BLOOD_THIRST = (caster, targets) -> {
        if (targets.isEmpty()) {
            exit(-1);
        }
        for (var target : targets) {
            target.onReceivedBloodThirst();
        }
    };

    /**
     * Tries resolving ability based on provided warrior name.
     *
     * @param warriorName
     * @return StatusOr Ability
     */
    public static StatusOr<Ability> resolveAbility(final String warriorName) {
        return switch (warriorName) {
            case "Disciple" -> new StatusOr<>(GODS_PLAN);
            case "Miraj" -> new StatusOr<>(SKYJACK);
            case "The Cursed One" -> new StatusOr<>(SHAPE_SHIFT);
            case "The Ripper" -> new StatusOr<>(WEAK_KNEES);
            case "Lord Royce" -> new StatusOr<>(SUB_ZERO);
            case "Empress Thorina" -> new StatusOr<>(LOW_BLOW);
            case "King Mudface" -> new StatusOr<>(EARTH_BORN);
            case "General Kocioraw" -> new StatusOr<>(BLOOD_THIRST);
            default -> new StatusOr<>(StatusCode.kUnknown);
        };
    }

    private AbilityHandler() { }
}
