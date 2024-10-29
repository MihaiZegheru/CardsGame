package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;

public class Minion extends Card {
    protected int attackDamage;

    public Minion(MinionData data) {
        super(data);
        this.attackDamage = data.getAttackDamage();
    }

    public Status Attack(Minion minion) {
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

    public int getAttackDamage() { return attackDamage; }
    @JsonIgnore
    public boolean isTank() { return ((MinionData)data).getType() == MinionType.kTank; }
    @JsonIgnore
    public boolean isDd() { return ((MinionData)data).getType() == MinionType.kDamageDealer; }

    @Override
    protected void OnDied() {
        GameManager.GetInstance().getGame().OnMinionDeath(this);
    }
}
