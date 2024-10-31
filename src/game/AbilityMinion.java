package game;

import game.datacollections.AbilityMinionData;
import status.Status;

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
}
