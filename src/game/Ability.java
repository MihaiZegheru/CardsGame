package game;

import status.Status;

@FunctionalInterface
public interface Ability {
    void useAbility(CasterMinion caster, Minion target);
}

