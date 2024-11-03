package game;

import game.datacollections.CasterMinionData;
import game.datacollections.HeroData;
import status.Status;
import status.StatusCode;

import java.util.ArrayList;

public class Hero extends Warrior implements Caster {

    public Hero(HeroData data, Army army) {
        super(data, army);
    }

    @Override
    public Status cast(ArrayList<Minion> minions) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Hero has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        ((HeroData)data).getAbility().useAbility(this, minions);
        hasAttacked = true;
        return Status.ok();
    }

    @Override
    protected void OnDied() { army.onHeroDied(); }
}
