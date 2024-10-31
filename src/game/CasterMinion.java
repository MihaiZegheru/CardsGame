package game;

import game.datacollections.AbilityMinionData;
import status.Status;
import status.StatusCode;

public class AbilityMinion extends Minion {

    public AbilityMinion(AbilityMinionData data, Army army) {
        super(data, army);
    }

    Status Attack(Minion minion) {
        Status attackStatus = super.Attack(minion);
        if (!attackStatus.isOk()) {
            return attackStatus;
        }
        return ((AbilityMinionData)data).getAbility().UseAbility(this);
    }

    @Override
    Status cast(Minion minion) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        minion.OnAttacked(attackDamage);
        hasAttacked = true;
        return Status.ok();
    }
}
