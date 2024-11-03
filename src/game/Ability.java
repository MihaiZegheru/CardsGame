package game;

import status.Status;

import java.util.ArrayList;

@FunctionalInterface
public interface Ability {
    void useAbility(Caster caster, ArrayList<Minion> targets);
}

