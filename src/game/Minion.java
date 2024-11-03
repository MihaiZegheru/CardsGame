package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.datacollections.CasterMinionData;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import utility.Math;

public class Minion extends Warrior {
    protected int attackDamage;

    protected Minion(MinionData data, Army army) {
        super(data, army);
        this.attackDamage = data.getAttackDamage();
    }

    @Override
    void TickTurn() {
        if (isFrozen) {
            frozenTimer--;
        }
        if (frozenTimer == 0) {
            isFrozen = false;
        }
    }

    Status Attack(Warrior warrior) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        warrior.OnAttacked(attackDamage);
        hasAttacked = true;
        return Status.ok();
    }

    static Minion BuildMinion(MinionData data, Army army) {
        if (data.getType().isAny(WarriorType.kDruid | WarriorType.kShadow)) {
            return new CasterMinion((CasterMinionData) data, army);
        } else {
            return new Minion(data, army);
        }
    }

    void onReceivedWeakKnees() { attackDamage = Math.Clamp(attackDamage - 2, 0, attackDamage); }
    void onReceivedSkyjack(int newHealth) {
        health = newHealth;
    }
    void onReceivedShapeshift() {
        int aux = attackDamage;
        attackDamage = health;
        health = aux;
        if (health <= 0) {
            OnDied();
        }
    }
    void onReceivedGodsPlan() { health += 2; }
    void onReceivedSubZero() { isFrozen = true; frozenTimer = 2; }
    void onReceivedLowBlow() { health = 0; OnDied(); }
    void onReceivedEarthBorn() { health += 1; }
    void onReceivedBloodThirst() { attackDamage += 1; }

    public int getAttackDamage() { return attackDamage; }

    @Override
    protected void OnDied() { army.OnMinionDied(this); }
}
