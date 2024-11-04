package game;

import java.util.ArrayList;

@FunctionalInterface
public interface Ability {

    /**
     * Interface for using the designed ability.
     *
     * @param caster
     * @param targets
     */
    void useAbility(Caster caster, ArrayList<Minion> targets);
}

