package game;

import status.Status;

@FunctionalInterface
public interface Ability {
    Status UseAbility(Warrior card);
}

