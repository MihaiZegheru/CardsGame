package game;

import game.datacollections.CasterMinionData;
import status.Status;
import status.StatusCode;

import java.util.ArrayList;

public final class CasterMinion extends Minion implements Caster {

    CasterMinion(final CasterMinionData data, final Army army) {
        super(data, army);
    }

    /**
     * Casts Ability over provided minions.
     *
     * @param minions length should be 1.
     * @return Status
     */
    @Override
    public Status cast(final ArrayList<Minion> minions) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        ((CasterMinionData) data).getAbility().useAbility(this, minions);
        hasAttacked = true;
        return Status.ok();
    }
}
