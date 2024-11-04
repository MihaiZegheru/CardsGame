package game;

import game.datacollections.CasterMinionData;
import game.datacollections.MinionData;
import status.Status;
import status.StatusCode;
import utility.Math;

public class Minion extends Warrior {
    protected int attackDamage;

    protected Minion(final MinionData data, final Army army) {
        super(data, army);
        this.attackDamage = data.getAttackDamage();
    }

    final Status attack(final Warrior warrior) {
        if (getHasAttacked()) {
            return new Status(StatusCode.kAborted, "Attacker card has already attacked this turn.");
        }
        if (getIsFrozen()) {
            return new Status(StatusCode.kAborted, "Attacker card is frozen.");
        }
        warrior.onAttacked(attackDamage);
        hasAttacked = true;
        return Status.ok();
    }

    static Minion buildMinion(final MinionData data, final Army army) {
        if (data.getType().isAny(WarriorType.getDruid() | WarriorType.getShadow())) {
            return new CasterMinion((CasterMinionData) data, army);
        } else {
            return new Minion(data, army);
        }
    }

    final void onReceivedWeakKnees() {
        attackDamage = Math.clamp(attackDamage - 2, 0, attackDamage);
    }

    final void onReceivedSkyjack(final int newHealth) {
        health = newHealth;
    }

    final void onReceivedShapeShift() {
        int aux = attackDamage;
        attackDamage = health;
        health = aux;
        if (health <= 0) {
            onDied();
        }
    }

    final void onReceivedGodsPlan() {
        health += 2;
    }

    final void onReceivedSubZero() {
        isFrozen = true;
        frozenTimer = 2;
    }

    final void onReceivedLowBlow() {
        health = 0;
        onDied();
    }

    final void onReceivedEarthBorn() {
        health += 1;
    }

    final void onReceivedBloodThirst() {
        attackDamage += 1;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    @Override
    protected final void onDied() {
        army.onMinionDied(this);
    }
}
