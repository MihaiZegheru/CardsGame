package game;

import game.datacollections.CasterMinionData;
import status.Status;
import status.StatusCode;

public class CasterMinion extends Minion implements Caster {

    public CasterMinion(CasterMinionData data, Army army) {
        super(data, army);
    }

    public Status cast(Minion minion) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        ((CasterMinionData)data).getAbility().UseAbility(this);
//        minion.OnAttacked(attackDamage);
        hasAttacked = true;
        return Status.ok();
    }
}
