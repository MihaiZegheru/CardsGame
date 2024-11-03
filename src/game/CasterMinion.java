package game;

import game.datacollections.CasterMinionData;
import status.Status;
import status.StatusCode;

import java.util.ArrayList;

public class CasterMinion extends Minion implements Caster {

    protected CasterMinion(CasterMinionData data, Army army) {
        super(data, army);
    }

    @Override
    public Status cast(ArrayList<Minion> minions) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        ((CasterMinionData)data).getAbility().useAbility(this, minions);
        hasAttacked = true;
        return Status.ok();
    }
}
